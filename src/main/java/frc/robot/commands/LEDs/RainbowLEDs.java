package frc.robot.commands.LEDs;

import edu.wpi.first.wpilibj.util.Color;
import frc.lib.commands.LoggedCommand;
import frc.robot.Constants;
import frc.robot.subsystems.LEDs;

public class RainbowLEDs extends LoggedCommand {
    private final LEDs leds;
    private int m_rainbowFirstPixelHue = 0;
    
    public RainbowLEDs(LEDs leds) {
      this.leds = leds;

      // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(leds);
    }

    @Override
    public void initialize() {
      super.initialize();
      leds.setAll(Color.kBlack);
    }

    @Override
    public void execute() {
        for(int i = 0; i < Constants.kLEDs.LED_LENGTH; i++) {
            final int hue = (m_rainbowFirstPixelHue + (i * 180 / Constants.kLEDs.LED_LENGTH )) % 180;
            leds.setLEDhsv(i, hue, 255, 128);
        }

        m_rainbowFirstPixelHue += 1.5;
        m_rainbowFirstPixelHue %= 180;

        leds.ledBuffer();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      leds.setAll(Color.kBlack);
      super.end(interrupted);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
}