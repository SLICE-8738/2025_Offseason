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
    public static Trigger cont1_buttonA = new JoystickButton(controller1, XboxController.Button.kA.value);
    public static Trigger cont1_buttonB = new JoystickButton(controller1, XboxController.Button.kB.value);
    public static Trigger cont1_buttonX = new JoystickButton(controller1, XboxController.Button.kX.value);
    public static Trigger cont1_buttonY = new JoystickButton(controller1, XboxController.Button.kY.value);
    public static Trigger cont1_leftBumper = new JoystickButton(controller1, 5);
    public static Trigger cont1_rightBumper = new JoystickButton(controller1, 6);
    public static Trigger cont1_leftTrigger =  new Trigger(() -> {return controller1.getRawAxis(2) > 0.35; });
    public static Trigger cont1_rightTrigger =  new Trigger(() -> {return controller1.getRawAxis(3) > 0.35; });
    public static Trigger cont1_share = new JoystickButton(controller1, 7);
    public static Trigger cont1_options = new JoystickButton(controller1, 8);
    public static Trigger cont1_leftStickClick1 = new JoystickButton(controller1, 9);
    public static Trigger cont1_rightStickClick1 = new JoystickButton(controller1, 10);
    public static Trigger cont1_controlPadRight1 = new POVButton(controller1, 90);
    public static Trigger cont1_controlPadUp1 = new POVButton(controller1, 0);
    public static Trigger cont1_controlPadLeft1 = new POVButton(controller1, 270);
    public static Trigger cont1_controlPadDown1 = new POVButton(controller1, 180);
    //public static Trigger cont1_screenshot = new JoystickButton(controller1, 11);
    //public static Trigger cont1_home = new JoystickButton(controller1, 12);

    /*public static Trigger cont1_leftTrigger1 = new JoystickButton(controller1, 7);
    public static Trigger cont1_rightTrigger1 = new JoystickButton(controller1, 8);
    public static Trigger cont1_psButton1 = new JoystickButton(controller1, 13);
    public static Trigger cont1_touchPad1 = new JoystickButton(controller1, 14);
    */

    /* Controller 2 Buttons */
    public static Trigger cont2_buttonX = new JoystickButton(controller2, XboxController.Button.kX.value);
    public static Trigger cont2_buttonA = new JoystickButton(controller2, XboxController.Button.kA.value);
    public static Trigger cont2_buttonB = new JoystickButton(controller2, XboxController.Button.kB.value);
    public static Trigger cont2_buttonY = new JoystickButton(controller2, XboxController.Button.kY.value);
    public static Trigger cont2_leftBumper2 = new JoystickButton(controller2, 5);
    public static Trigger cont2_rightBumper2 = new JoystickButton(controller2, 6);
    public static Trigger cont2_leftTrigger2 = new JoystickButton(controller2, 7);
    public static Trigger cont2_rightTrigger2 = new JoystickButton(controller2, 8);
    public static Trigger cont2_back = new JoystickButton(controller2, 9);
    public static Trigger cont2_start = new JoystickButton(controller2, 10);
    public static Trigger cont2_leftStickClick2 = new JoystickButton(controller2, 11);
    public static Trigger cont2_rightStickClick2 = new JoystickButton(controller2, 12);
    public static Trigger cont2_psButton2 = new JoystickButton(controller2, 13);
    public static Trigger cont2_controlPadRight2 = new POVButton(controller2, 90);
    public static Trigger cont2_controlPadUp2 = new POVButton(controller2, 0);
    public static Trigger cont2_controlPadLeft2 = new POVButton(controller2, 270);
    public static Trigger cont2_controlPadDown2 = new POVButton(controller2, 180);
    
}
