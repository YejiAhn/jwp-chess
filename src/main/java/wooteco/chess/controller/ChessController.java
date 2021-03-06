package wooteco.chess.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import wooteco.chess.domain.Board;
import wooteco.chess.service.ChessService;

@Controller
public class ChessController {
    private final ChessService service;

    public ChessController(ChessService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String entrance(@RequestParam(defaultValue = "") String error, Model model) {
        model.addAttribute("roomNames", service.getAllRoomNames());
        if (!error.isEmpty()) {
            model.addAttribute("error", error);
        }
        return "chess-before-start";
    }

    @PostMapping("/make-room")
    public String loadNewGame(@RequestParam(value = "new-room-name") String name, Model model,
        RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("room", service.createGame(name));
            return "chess-running";
        } catch (RuntimeException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/game")
    public String showGame(@RequestParam(value = "room-id") int roomId, @RequestParam(defaultValue = "") String error,
        RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("room", service.findGame(roomId));
        if (service.isGameEnd(roomId)) {
            redirectAttributes.addAttribute("room-id", roomId);
            return "redirect:/result";
        }
        if (!error.isEmpty()) {
            model.addAttribute("error", error);
        }
        return "chess-running";
    }

    @GetMapping("/result")
    public String showResult(@RequestParam(value = "room-id") int id, Model model) {
        model.addAttribute("result", service.endGame(id));
        return "chess-result";
    }

    @PostMapping("continue-game")
    public String continueGame(@RequestParam(value = "existing-room-name") String name,
        RedirectAttributes redirectAttributes) {
        try {
            int id = service.findIdByName(name);
            redirectAttributes.addAttribute("room-id", id);
            if (service.isGameEnd(id)) {
                return "redirect:/result";
            }
            return "redirect:/game";
        } catch (RuntimeException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/move")
    public String executeMove(@RequestParam Map<String, String> parameters, RedirectAttributes redirectAttributes) {
        int roomId = Integer.parseInt(parameters.get("room-id"));
        Board board = service.getSavedBoard(roomId);
        String source = parameters.get("source");
        String destination = parameters.get("destination");
        redirectAttributes.addAttribute("room-id", roomId);
        try {
            service.processMoveInput(board, source, destination, roomId);
        } catch (RuntimeException e) {
            redirectAttributes.addAttribute("error", Boolean.TRUE);
        }
        return "redirect:/game";
    }

    @PostMapping("/initialize")
    public String initializeGame(@RequestParam(value = "room-id") int roomId, RedirectAttributes redirectAttributes) {
        service.initBoard(roomId);
        redirectAttributes.addAttribute("room-id", roomId);
        return "redirect:/game";
    }
}
