package org.mark.chess.config;

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
    public KingLogic getKingLogic() {
        return new KingLogic();
    }

    @Bean
    public GameService getGameService() {
        return new GameService();
    }

}
