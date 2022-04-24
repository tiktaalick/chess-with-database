package org.mark.chess.config;

import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.factory.InitialPieceFactory;
import org.mark.chess.factory.PieceFactory;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.logic.BishopLogic;
import org.mark.chess.logic.BoardLogic;
import org.mark.chess.logic.ButtonLogic;
import org.mark.chess.logic.FieldLogic;
import org.mark.chess.logic.GameLogic;
import org.mark.chess.logic.GridLogic;
import org.mark.chess.logic.KingLogic;
import org.mark.chess.logic.KnightLogic;
import org.mark.chess.logic.MoveLogic;
import org.mark.chess.logic.PawnLogic;
import org.mark.chess.logic.QueenLogic;
import org.mark.chess.logic.RookLogic;
import org.mark.chess.service.GameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ApplicationFactory getApplicationFactory() {
        return new ApplicationFactory();
    }

    @Bean
    public BackgroundColorFactory getBackgroundColorFactory() {
        return new BackgroundColorFactory();
    }

    @Bean
    public BishopLogic getBishopLogic() {
        return new BishopLogic();
    }

    @Bean
    public BoardLogic getBoardLogic() {
        return new BoardLogic();
    }

    @Bean
    public ButtonLogic getButtonLogic() {
        return new ButtonLogic();
    }

    @Bean
    public FieldLogic getFieldLogic() {
        return new FieldLogic();
    }

    @Bean
    public GameLogic getGameLogic() {
        return new GameLogic();
    }

    @Bean
    public GameService getGameService() {
        return new GameService();
    }

    @Bean
    public GridLogic getGridLogic() {
        return new GridLogic();
    }

    @Bean
    public InitialPieceFactory getInitialPieceFactory() {
        return new InitialPieceFactory();
    }

    @Bean
    public KingLogic getKingLogic() {
        return new KingLogic();
    }

    @Bean
    public KnightLogic getKnightLogic() {
        return new KnightLogic();
    }

    @Bean
    public MoveLogic getMoveLogic() {
        return new MoveLogic();
    }

    @Bean
    public PawnLogic getPawnLogic() {
        return new PawnLogic();
    }

    @Bean
    public PieceFactory getPieceFactory() {
        return new PieceFactory();
    }

    @Bean
    public PieceLogicFactory getPieceLogicFactory() {
        return new PieceLogicFactory();
    }

    @Bean
    public QueenLogic getQueenLogic() {
        return new QueenLogic();
    }

    @Bean
    public RookLogic getRookLogic() {
        return new RookLogic();
    }
}
