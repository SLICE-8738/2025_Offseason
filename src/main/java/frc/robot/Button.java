package frc.robot;

import java.util.function.BooleanSupplier;

import org.w3c.dom.xpath.XPathNSResolver;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.event.BooleanEvent;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Button {

    /* Controllers */
    public static XboxController controller1 = new XboxController(0);
    public static XboxController controller2 = new XboxController(1);

    /* Controller 1 Buttons */
    public static Trigger buttonA = new JoystickButton(controller1, XboxController.Button.kA.value);
    public static Trigger buttonB = new JoystickButton(controller1, XboxController.Button.kB.value);
    public static Trigger buttonX = new JoystickButton(controller1, XboxController.Button.kX.value);
    public static Trigger buttonY = new JoystickButton(controller1, XboxController.Button.kY.value);
    public static Trigger leftBumper1 = new JoystickButton(controller1, 5);
    public static Trigger rightBumper1 = new JoystickButton(controller1, 6);
    public static Trigger leftTrigger1 = new Trigger(() -> { return controller1.getRawAxis(2) > 0.35; });
    public static Trigger rightTrigger1 = new Trigger(() -> { return controller1.getRawAxis(3) > 0.35; });
    public static Trigger share = new JoystickButton(controller1, 7);
    public static Trigger options = new JoystickButton(controller1, 8);
    public static Trigger leftStickClick1 = new JoystickButton(controller1, 9);
    public static Trigger rightStickClick1 = new JoystickButton(controller1, 10);
    //public static Trigger psButton1 = new JoystickButton(controller1, 13);
    //public static Trigger touchPad1 = new JoystickButton(controller1, 14);
    public static Trigger controlPadRight1 = new POVButton(controller1, 90);
    public static Trigger controlPadUp1 = new POVButton(controller1, 0);
    public static Trigger controlPadLeft1 = new POVButton(controller1, 270);
    public static Trigger controlPadDown1 = new POVButton(controller1, 180);

    /* Controller 2 Buttons */
    public static Trigger buttonX2 = new JoystickButton(controller2, XboxController.Button.kX.value);
    public static Trigger buttonA2 = new JoystickButton(controller2, XboxController.Button.kA.value);
    public static Trigger buttonB2 = new JoystickButton(controller2, XboxController.Button.kB.value);
    public static Trigger buttonY2 = new JoystickButton(controller2, XboxController.Button.kY.value);
    public static Trigger leftBumper2 = new JoystickButton(controller2, 5);
    public static Trigger rightBumper2 = new JoystickButton(controller2, 6);
    public static Trigger leftTrigger2 = new Trigger(() -> { return controller2.getRawAxis(2) > 0.35; });
    public static Trigger rightTrigger2 = new Trigger(() -> { return controller2.getRawAxis(3) > 0.35; });
    public static Trigger back = new JoystickButton(controller2, 7);
    public static Trigger start = new JoystickButton(controller2, 8);
    public static Trigger leftStickClick2 = new JoystickButton(controller2, 9);
    public static Trigger rightStickClick2 = new JoystickButton(controller2, 10);
    //public static Trigger psButton2 = new JoystickButton(controller2, 13);
    public static Trigger controlPadRight2 = new POVButton(controller2, 90);
    public static Trigger controlPadUp2 = new POVButton(controller2, 0);
    public static Trigger controlPadLeft2 = new POVButton(controller2, 270);
    public static Trigger controlPadDown2 = new POVButton(controller2, 180);

}
