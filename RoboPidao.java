package pidao;
import robocode.*;
import java.awt.Color;

public class RoboPidao extends AdvancedRobot {
    // Variáveis de estado
    private boolean enemyClose = false;
    private boolean lowHealth = false;
    
    // Variáveis para guardar dados do inimigo
    private double enemyBearing = 0;
    private double enemyDistance = 0;
    private boolean targetSpotted = false;

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        
        double width = getBattleFieldWidth();
        double height = getBattleFieldHeight();
            
        setColors(Color.green, Color.yellow, Color.blue);

        while(true) {
            // Se não viu nenhum inimigo, gira o radar para procurar
            if (!targetSpotted) {
                setTurnRadarRight(360);
            }

            if (enemyClose) {
                fire(3);
            }

            if (getEnergy() <= 30) {
                lowHealth = true;
            }
            
            // LOGGING
            out.println("--- Status do Turno ---");
            out.println("Posição: " + getX() + ", " + getY());
            out.println("Inimigo Perto: " + enemyClose);
            out.println("Pouca Vida: " + lowHealth);
            out.println("Distancia Inimigo: " + enemyDistance);
            
            // Reseta o sinalizador para o próximo turno procurar novamente
            targetSpotted = false;
            
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // Sinaliza que encontrou um alvo neste turno
        targetSpotted = true;
        enemyDistance = e.getDistance();

        // Atualiza o estado de proximidade do inimigo
        if (enemyDistance <= 200) {
            enemyClose = true;
        } else {
            enemyClose = false;
        }

        // Foca o Radar no inimigo
        double radarTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
        setTurnRadarRightRadians(robocode.util.Utils.normalRelativeAngle(radarTurn));

        // Segue o alvo
        double radarHeading = getRadarHeading();
        double gunHeading = getGunHeading();
        double tankHeading = getHeading();

        double angleDifference = radarHeading - gunHeading;
        double tank_angleDifference = radarHeading - tankHeading;        
        
        angleDifference = normalRelativeAngleDegrees(angleDifference, false);
        tank_angleDifference = normalRelativeAngleDegrees(tank_angleDifference, enemyClose);
        
        // Agenda comandos de direcao
        setTurnGunRight(angleDifference);
        setTurnRight(tank_angleDifference);
        setAhead(200); 
    }
		
    // Função auxiliar para normalizar ângulos em graus
	// TODO melhorar a "orbita" mirando no inimigo, evitar bater
    public double normalRelativeAngleDegrees(double angle, boolean isTrack) {
        double normalized = angle % 360;
        if (normalized >= 180) {
            normalized -= 360;
			if (isTrack) {
				out.println("normal-");
				normalized += 90;
			}
        }
        if (normalized < -180) {
            normalized += 360;
			if (isTrack) {
				out.println("normal+");
				normalized -= 90;
			}
        }
        return normalized;
    }
}
