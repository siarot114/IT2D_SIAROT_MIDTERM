package com.siarot;

import com.siarot.dao.ScenarioDao;
import com.siarot.ui.ScenarioMenu;

public class Main {
    public static void main(String[] args) {
        ScenarioDao dao = new ScenarioDao();
        new ScenarioMenu(dao).run();
    }
}
