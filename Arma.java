package pidao;
import robocode.*;
//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * Arma - a robot by (your name here)
 */
public class Arma extends AdvancedRobot
{
	/**
	 * run: Arma's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		setAdjustGunForRobotTurn(true);
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop


		while(true) {
			// Replace the next 4 lines with any behavior you would like
			turnRadarRightRadians(Double.POSITIVE_INFINITY);
			execute();
			}
		}
		
		// Função auxiliar para normalizar ângulos em graus
		private double normalRelativeAngleDegrees(double angle) {
    		double normalized = angle % 360;
    		if (normalized >= 180) {
    		    normalized -= 360;
    		}
    		if (normalized < -180) {
    		    normalized += 360;
    		}
    		return normalized;
		}


	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
    	double radarTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
	   	setTurnRadarRightRadians(robocode.util.Utils.normalRelativeAngle(radarTurn));
		// Calcula o ângulo necessário para o canhão alcançar o radar
		double gunHeading = getGunHeading();
		double radarHeading = getRadarHeading();
		double trackHeading = getHeading();
		double angleDifference = radarHeading - gunHeading;
		double tank_angleDifference = radarHeading - trackHeading;        
		
		// Normaliza o ângulo para garantir que gire pelo caminho mais curto (-180 a 180 graus)
		angleDifference = normalRelativeAngleDegrees(angleDifference);
		tank_angleDifference = normalRelativeAngleDegrees(tank_angleDifference);
		// Move o canhão para a posição do radar
		if (e.getDistance() >= 100) {
		angleDifference = angleDifference + 30;
		}
		setTurnGunRight(angleDifference);
		setTurnRight(tank_angleDifference);
		
		setAhead(100);
		execute();
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		turnLeft(20);
	}
	public void onHitRobot(HitRobotEvent e) {
		fire(3);
	}
}
