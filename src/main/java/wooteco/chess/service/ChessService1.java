package wooteco.chess.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Pieces;
import wooteco.chess.domain.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.exception.DuplicateRoomNameException;
import wooteco.chess.repository.PieceEntity;
import wooteco.chess.repository.RoomEntity;
import wooteco.chess.repository.RoomRepository;

@Service
public class ChessService1 {
    private final RoomRepository roomRepository;

    public ChessService1(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // public void initBoard(int roomId) {
    //     roomDao.updateTurn(roomId, Team.WHITE);
    //     boardDao.removeAll(roomId);
    //     boardDao.saveBoard(new Board(), roomId);
    // }

    public void initBoard(int roomId) {
        Set<PieceEntity> pieceEntities = new HashSet<>();
        for (Piece alivePiece : new Board().getPieces().getAlivePieces()) {
            pieceEntities.add(new PieceEntity(alivePiece.getPosition(), alivePiece.toString(), alivePiece.getTeam()));
        }
        roomRepository.save(new RoomEntity((long)roomId, findNameById(roomId), Team.WHITE));
    }

    public boolean isPresentRoom(String name) {
        return roomRepository.findIdByName(name).isPresent();
    }

    // public int createBoard(String name) {
    //     validateRoomName(name);
    //     roomDao.createRoom(name);
    //     int roomId = roomDao.findRoomIdByName(name)
    //         .orElseThrow(AssertionError::new);
    //     boardDao.saveBoard(new Board(), roomId);
    //     return roomId;
    // }

    private void validateRoomName(String name) {
        if (isPresentRoom(name)) {
            throw new DuplicateRoomNameException("존재하는 방 이름입니다.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("방 이름을 입력해주세요.");
        }
    }

    public Board getSavedBoard(int roomId) {
        RoomEntity roomEntity = roomRepository.findById((long)roomId)
            .orElseThrow(()-> new IllegalArgumentException("해당 id의 방이 존재하지 않습니다."));
        return new Board(new Pieces(roomEntity.getPieceEntities()), roomEntity.getTurn());
    }

    // public void processMoveInput(Board board, String source, String destination, int roomId) {
    //     board.movePiece(new Position(source), new Position(destination));
    //     Pieces pieces = board.getPieces();
    //     Piece destinationPiece = pieces.findByPosition(new Position(destination));
    //     if (destinationPiece == null) {
    //         boardDao.editPiece(source, destination, roomId);
    //     }
    //     if (destinationPiece != null) {
    //         boardDao.removePiece(destination, roomId);
    //         boardDao.editPiece(source, destination, roomId);
    //     }
    //     roomDao.updateTurn(roomId, board.getTurn());
    // }

    public void processMoveInput(Board board, String source, String destination, int roomId) {
        board.movePiece(new Position(source), new Position(destination));
        Pieces pieces = board.getPieces();
        Piece destinationPiece = pieces.findByPosition(new Position(destination));
        if (destinationPiece == null) {

        }
        if (destinationPiece != null) {

        }
    }

    public int findIdByName(String name) {
        return roomRepository.findIdByName(name).orElseThrow(()->new IllegalArgumentException("해당 이름의 방이 존재하지 않습니다."));
    }

    public String findNameById(int roomId) {
        return roomRepository.findById((long)roomId)
            .map(RoomEntity::getName)
            .orElseThrow(() -> new IllegalArgumentException("해당 id의 방이 존재하지 않습니다."));
    }
}
