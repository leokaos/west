package org.west.entidades;

import java.awt.Color;
import java.util.Date;

public class StatusTarefa implements Comparable<StatusTarefa> {

    public static StatusTarefa VENCIDA = new StatusTarefa(new Color(251, 120, 120), 1);
    public static StatusTarefa SEM_ACAO = new StatusTarefa(new Color(255, 145, 0), 2);
    public static StatusTarefa ESGOTANDO = new StatusTarefa(new Color(242, 245, 113), 3);
    public static StatusTarefa NORMAL = new StatusTarefa(Color.white, 4);
    public static StatusTarefa TERMINADO = new StatusTarefa(new Color(169, 249, 155), 5);
    private final Color color;
    private final Integer prioridade;

    private StatusTarefa(Color color, Integer prioridade) {
        this.color = color;
        this.prioridade = prioridade;
    }

    public Color getColor() {
        return color;
    }

    public static StatusTarefa parseTarefa(Tarefa tarefa) {
        if (tarefa.getTerminado()) {
            return TERMINADO;
        } else if (isVencida(tarefa)) {
            return VENCIDA;
        } else if (isEsgotando(tarefa)) {
            return ESGOTANDO;
        } else if (isSemAcao(tarefa)) {
            return SEM_ACAO;
        } else {
            return NORMAL;
        }
    }

    private static boolean isEsgotando(Tarefa tarefa) {
        double tempo = WestPersistentManager.getDateServer().getTime() - tarefa.getDataInicio().getTime();
        tempo /= (tarefa.getPrevisaoTermino().getTime() - tarefa.getDataInicio().getTime());

        return tempo > 0.7D;
    }

    private static boolean isSemAcao(Tarefa tarefa) {
        Date hoje = WestPersistentManager.getDateServer();

        if (hoje.before(tarefa.getAviso())) {
            return false;
        } else {
            return hoje.after(tarefa.getDataAtualizacao());
        }
    }

    private static boolean isVencida(Tarefa tarefa) {
        Date dataServer = WestPersistentManager.getDateServer();
        return tarefa.getPrevisaoTermino().before(dataServer);
    }

    @Override
    public int compareTo(StatusTarefa o) {
        return prioridade.compareTo(o.prioridade);
    }
}
