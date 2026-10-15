package com.ssafy.diet.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.diet.dto.Diet;
import com.ssafy.diet.dto.Food;

import diet.DietManager;
import diet.DietManagerImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 식단 Controller — /diet?action=xxx
 *
 * GET  list        전체 목록 (F402)
 * GET  detail      상세 + 분석 (F402, F405)
 * GET  writeForm   작성 화면 (+ 음식 검색)
 * GET  updateForm  수정 화면 (+ 음식 검색)
 * POST write       등록 (F401)
 * POST update      수정 (F403)
 * POST delete      삭제 (F404)
 */
@WebServlet(urlPatterns = "/diet", loadOnStartup = 1)
public class DietController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DietManager manager = DietManagerImpl.getInstance();

	/** 서버 시작 시 1번 — 콘솔 버전 main() 의 fileInput()/loadFoods() 역할 */
	@Override
	public void init() throws ServletException {
		String dataDir = getServletContext().getRealPath("/WEB-INF/data");
		manager.init(dataDir);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		if (action == null) action = "list";

		switch (action) {
		case "list":       list(request, response); break;
		case "detail":     detail(request, response); break;
		case "writeForm":  writeForm(request, response); break;
		case "updateForm": updateForm(request, response); break;
		default: response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");   // 한글 깨짐 방지 (getParameter 전에!)

		String action = request.getParameter("action");
		if (action == null) action = "";

		switch (action) {
		case "write":  write(request, response); break;
		case "update": update(request, response); break;
		case "delete": delete(request, response); break;
		default: response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	// ================= 조회 =================

	private void list(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("dietList", manager.getDiets());
		forward(request, response, "list");
	}

	private void detail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Diet diet = manager.getDiet(toInt(request.getParameter("id")));
		if (diet == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "식단을 찾을 수 없습니다.");
			return;
		}
		request.setAttribute("diet", diet);
		request.setAttribute("analysis", manager.analyze(diet));
		forward(request, response, "detail");
	}

	// ================= 작성 =================

	private void writeForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 검색 버튼으로 다시 들어온 경우, 입력 중이던 값(날짜/유형/선택 음식)을 유지
		request.setAttribute("diet", readDiet(request, 0));
		request.setAttribute("foodList", manager.searchFoods(request.getParameter("keyword")));
		forward(request, response, "form");
	}

	private void write(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Diet diet = readDiet(request, manager.getNextId());

		String error = validate(diet);
		if (error != null) {
			request.setAttribute("errorMsg", error);
			request.setAttribute("diet", diet);
			forward(request, response, "form");
			return;
		}

		manager.add(diet);
		// 등록 후 redirect → 새로고침해도 중복 등록되지 않음 (PRG 패턴)
		response.sendRedirect(request.getContextPath() + "/diet?action=detail&id=" + diet.getId());
	}

	// ================= 수정 =================

	private void updateForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = toInt(request.getParameter("id"));
		Diet saved = manager.getDiet(id);
		if (saved == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "식단을 찾을 수 없습니다.");
			return;
		}
		// 처음 열면 저장된 값, 검색 버튼으로 다시 열면 입력 중인 값
		boolean searching = request.getParameter("date") != null;
		request.setAttribute("diet", searching ? readDiet(request, id) : saved);
		request.setAttribute("foodList", manager.searchFoods(request.getParameter("keyword")));
		forward(request, response, "form");
	}

	private void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Diet diet = readDiet(request, toInt(request.getParameter("id")));

		String error = validate(diet);
		if (error != null) {
			request.setAttribute("errorMsg", error);
			request.setAttribute("diet", diet);
			forward(request, response, "form");
			return;
		}

		if (!manager.updateDiet(diet)) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "식단을 찾을 수 없습니다.");
			return;
		}
		response.sendRedirect(request.getContextPath() + "/diet?action=detail&id=" + diet.getId());
	}

	// ================= 삭제 =================

	private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		manager.deleteDiet(toInt(request.getParameter("id")));
		response.sendRedirect(request.getContextPath() + "/diet?action=list");
	}

	// ================= 공통 =================

	/** 폼 파라미터 → Diet (음식은 이름으로 음식 DB 에서 찾아 넣는다) */
	private Diet readDiet(HttpServletRequest request, int id) {
		List<Food> foods = new ArrayList<>();
		String[] foodNames = request.getParameterValues("foodName");
		if (foodNames != null) {
			for (String name : foodNames) {
				Food f = manager.getFood(name);
				if (f != null) foods.add(f);
			}
		}
		return new Diet(id, request.getParameter("date"), request.getParameter("type"), foods);
	}

	/** 입력 검증 — 문제가 있으면 메시지, 없으면 null */
	private String validate(Diet diet) {
		if (diet.getDate() == null || !diet.getDate().matches("\\d{4}-\\d{2}-\\d{2}")) {
			return "날짜를 입력해주세요.";
		}
		if (diet.getType() == null || diet.getType().isBlank()) {
			return "식사 유형을 선택해주세요.";
		}
		if (diet.getFoods().isEmpty()) {
			return "음식을 1개 이상 선택해주세요.";
		}
		return null;
	}

	private void forward(HttpServletRequest request, HttpServletResponse response, String view)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/diet/" + view + ".jsp").forward(request, response);
	}

	private int toInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}