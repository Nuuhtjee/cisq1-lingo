Feature: Practicing for Lingo
  As a player,
  I want to guess 5,6,7 letter words,
  in order to prepare for Lingo

  Scenario: Start a new game
    Given i am playing a game
    And the round was won
    And the last word was "5" letters
    When I start a new round
    Then the word to guess has "6" letters


  Scenario Outline: Start a new round
    Given I am playing a game
    And the round was won
    And the last word had "<previous length>" letters
    When I start a new round
    Then the word to guess has "<next length>" letters

    Examples:
      | previous length | next length |
      | 5               | 6           |
      | 6               | 7           |
      | 7               | 5

    # Failure path
    Given I am playing a game
    And the round was lost
    Then I cannot start a new round


  Scenario Outline: Guessing a word
    Given | I am in a game
    And | I have been given a "<word>"
    When | I input a "<guess>" word
    Then | I expect to get "<feedback>"

    Examples:
      | word | guess       | feedback                                             |
      | BAARD| BERGEN      | INVALID, INVALID, INVALID, INVALID, INVALID, INVALID |
      | BAARD| BONJE       | CORRECT, ABSENT, ABSENT, ABSENT ,ABSENT              |
      | BAARD| BARST       | CORRECT, CORRECT, PRESENT, ABSENT, ABSENT            |
      | BAARD| DRAAD       | ABSENT, PRESENT, CORRECT, PRESENT, CORRECT           |
      | BAARD| BAARD       | CORRECT, CORRECT, CORRECT, CORRECT, CORRECT          |



