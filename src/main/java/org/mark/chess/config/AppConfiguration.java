package org.mark.chess.config;

import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.factory.PieceFactory;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.logic.*;
import org.mark.chess.service.GameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public BoardLogic getBoardLogic() {
        return new BoardLogic();
    }

    @Bean
    public GameLogic getGameLogic() {
        return new GameLogic();
    }

    @Bean
    public GridLogic getGridLogic() {
        return new GridLogic();
    }

    @Bean
    public FieldLogic getFieldLogic() {
        return new FieldLogic();
    }

    @Bean
    public PieceLogicFactory getPieceLogicFactory() {
        return new PieceLogicFactory();
    }

    @Bean
    public PieceFactory getPieceFactory() {
        return new PieceFactory();
    }

    @Bean
    public ApplicationFactory getApplicationFactory() {
        return new ApplicationFactory();
    }

    @Bean
    public KingLogic getKingLogic() {
        return new KingLogic();
    }

    @Bean
    public QueenLogic getQueenLogic() {
        return new QueenLogic();
    }

    @Bean
    public RookLogic getRookLogic() {
        return new RookLogic();
    }

    @Bean
    public BishopLogic getBishopLogic() {
        return new BishopLogic();
    }

    @Bean
    public KnightLogic getKnightLogic() {
        return new KnightLogic();
    }

    @Bean
    public PawnLogic getPawnLogic() {
        return new PawnLogic();
    }

    @Bean
    public MoveLogic getMoveLogic() {
        return new MoveLogic();
    }

    @Bean
    public GameService getGameService() {
        return new GameService();
    }

}
