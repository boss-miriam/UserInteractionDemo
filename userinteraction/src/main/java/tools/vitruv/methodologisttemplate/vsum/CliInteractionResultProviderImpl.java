package tools.vitruv.methodologisttemplate.vsum;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import tools.vitruv.change.interaction.InteractionResultProvider;
import tools.vitruv.change.interaction.UserInteractionOptions;

/**
 * A result provider based on CLI in- and output to make the requested input.
 *
 * @author Miriam Boss
 */
public class CliInteractionResultProviderImpl implements InteractionResultProvider {
  private final Scanner reader;
  private final PrintWriter writer;

  /** Generates a new Result provider that operates on the CLI. */
  public CliInteractionResultProviderImpl() {
    Console console = System.console();
    if ((console != null)) {
      this.reader = new Scanner(console.reader());
      this.writer = console.writer();
      System.out.print("Console initialized");
    } else {
      this.reader = new Scanner(System.in);
      this.writer = new PrintWriter(System.out);
      System.out.println("System In initialized");
      this.writer.println("Test");
    }
  }

  @Override
  public boolean getConfirmationInteractionResult(
      final UserInteractionOptions.WindowModality windowModality,
      final String title,
      final String message,
      final String positiveDecisionText,
      final String negativeDecisionText,
      final String cancelDecisionText) {
    this.writer.println(title + ": " + message + " [y/n]?\n");
    String s = this.reader.nextLine();
    s = s.toLowerCase().strip();
    return (s.equals("yes") || s.equals("y") || s.equals("ja") || s.equals("j"));
  }

  @Override
  public void getNotificationInteractionResult(
      final UserInteractionOptions.WindowModality windowModality,
      final String title,
      final String message,
      final String positiveDecisionText,
      final UserInteractionOptions.NotificationType notificationType) {
    this.writer.println(notificationType + " " + title + ": " + message+"\n");
  }

  @Override
  public String getTextInputInteractionResult(
      final UserInteractionOptions.WindowModality windowModality,
      final String title,
      final String message,
      final String positiveDecisionText,
      final String cancelDecisionText,
      final UserInteractionOptions.InputValidator inputValidator) {
    this.writer.println(title + ": " + message+"\n");
    String s = this.reader.nextLine();
    while ((!inputValidator.isInputValid(s)) || (!s.strip().equalsIgnoreCase(cancelDecisionText))) {
      this.writer.println(
          "\""
              + s
              + "\" is not a valid input, please try again or type \""
              + cancelDecisionText
              + "\" to abort the input\n");
      s = this.reader.nextLine();
    }
    return s;
  }

  @Override
  public int getMultipleChoiceSingleSelectionInteractionResult(
      final UserInteractionOptions.WindowModality windowModality,
      final String title,
      final String message,
      final String positiveDecisionText,
      final String cancelDecisionText,
      final Iterable<String> choices) {
    StringBuilder sb = new StringBuilder(title + ": " + message + "\nChoices:\n");
    int i = 0;
    for (String choice : choices) {
      sb.append("(").append(i).append(") ").append(choice).append("\n");
      choices.iterator().next();
      i++;
    }
    sb.append("Please select one option based on the index displayed or type \"")
        .append(cancelDecisionText)
        .append("\" to abort the operation\n");
    this.writer.println(sb);
    String s = this.reader.nextLine();
    while (true) {
      if (s.strip().equalsIgnoreCase(cancelDecisionText)) { // Abort
        return 0;
      }
      try {
        int response = Integer.parseInt(s); // Input given
        if (response >= 0 && response < i) { // Input in the valid range
          return response;
        } else {
          this.writer.println("\"" + response + "\" is not in the valid range, try again");
          s = this.reader.nextLine();
        }
      } catch (NumberFormatException e) { // Not a number as input
        this.writer.println("\"" + s + "\" is not a valid number, try again");
        s = this.reader.nextLine();
      }
    }
  }

  @Override
  public Iterable<Integer> getMultipleChoiceMultipleSelectionInteractionResult(
      final UserInteractionOptions.WindowModality windowModality,
      final String title,
      final String message,
      final String positiveDecisionText,
      final String cancelDecisionText,
      final Iterable<String> choices) {
    StringBuilder sb = new StringBuilder(title + ": " + message + "\nChoices:\n");
    int i = 0;
    while (choices.iterator().hasNext()) {
      String choice = choices.toString();
      sb.append("(").append(i).append(") ").append(choice).append("\n");
      choices.iterator().next();
      i++;
    }
    sb.append("Please select options based on the index displayed (comma seperated) or type \"")
        .append(cancelDecisionText)
        .append("\" to abort the operation\n");
    this.writer.println(sb.toString().strip());
    String s = this.reader.nextLine();
    while (true) {
      if (s.strip().equalsIgnoreCase(cancelDecisionText)) { // Abort
        return Collections.singleton(0);
      }
      String[] fragments = s.strip().split(",");
      ArrayList<Integer> result = new ArrayList<>();
      int correctlyParsedFragments = 0;
      for (String fragment : fragments) {
        try {
          int fragmentInt = Integer.parseInt(fragment);
          if (!(fragmentInt >= 0 && fragmentInt < i)) {
            if (!result.contains(fragmentInt)) {
              result.add(fragmentInt);
            }
            correctlyParsedFragments++;
          } else {
            this.writer.println("\"" + fragmentInt + "\" is not in the valid range, try again\n");
            s = this.reader.nextLine();
            break;
          }
        } catch (NumberFormatException e) {
          this.writer.println("\"" + fragment + "\" is not a valid number, try again\n");
          s = this.reader.nextLine();
          break;
        }
      }
      if (fragments.length == correctlyParsedFragments) {
        return result;
      }
    }
  }
}
