import java.util.Random;

import javax.print.attribute.standard.NumberOfDocuments;

/**
 * Defines the central game model for the Top Trumps card game implementation
 * Initialises the Players and Decks then runs the main game logic responding to
 * actions through the GUI.
 *
 * Written by Team HACKT for MSc SD Team Project
 */

public class Game {

	protected static final int HUMAN_PLAYER = 0;	// Human player is always first position in player array
	private boolean gameInProgress = false;
	private Deck communalPile = new Deck();
	private Deck mainDeck;
	private Player[] player;
	private Player currentPlayer;
	private int numOfCompPlayers;
	private int totalRounds;
	private int draws;

	/**
	 * Constructor for a Game instance
	 * @param mainDeck a Deck instance containing the full deck of cards as read in
	 * from a seperate file */
	public Game(Deck mainDeck) {
		this.mainDeck = mainDeck;
	}

	/**
	 * Initialises a new game state and begins the first round of a new game
	 * @param numOfCompPlayers the number of opponents chosen by the human player when they start the game */
	private void startGame(int numOfCompPlayers) {
		this.numOfCompPlayers = numOfCompPlayers;
		gameInProgress = true;
		totalRounds = 0;
		draws = 0;
		player = new Player[numOfCompPlayers + 1];
		player[HUMAN_PLAYER].setHuman(true);		//**** Relies on an external unimplemented or changeable design
		initPlayerDecks();
		currentPlayer = player[randomiseFirstPlayer()];
		nextRound();
	}

	/**
	 * @Return a randomised int within the range of active players */
	private int randomiseFirstPlayer() {
		Random r = new Random();
		return r.nextInt(numOfCompPlayers + 1);
	}

	/**
	 * Iterates through each player dealing them the next top card from the deck until there are no cards left */
	private void initPlayerDecks() {
		mainDeck.shuffleDeck();
		for(int i = 0, j = 0; i < mainDeck.getLength(); i++) {				//**** Relies on an external unimplemented or changeable design 
			player[j].getDeck().addCard(mainDeck.getNextCard());			//**** Relies on an external unimplemented or changeable design 
			if(j <= numOfCompPlayers) {
				j++;
			}
			else {
				j = 0;
			}
		}
	}

	/**
	 * Begins the next round; if currentPlayer is human it will wait for input, otherwise it will call for the computer 
	 * player to choose a category then initiate the comparison between players */
	private void nextRound() {
		totalRounds++;
		
		switch(currentPlayer.isHuman()) {			//**** Relies on an external unimplemented or changeable design
			case true: break;				// Wait for human player to confirm choice and advance to calculateRoundResult() directly
			case false: calculateRoundResult(currentPlayer.chooseCategory());		// Computer chooses category and advance to calculateRoundResult()
		}
	}

	/**
	 * Finds the player with the highest value in the chosen category then advances to roundWon(), or roundDraw() if multiple highest values
	 * @param chosenCategory an int which represents the array position of the chosen category on the card instance */
	private void calculateRoundResult(int chosenCategory) {

		// Assume current player will win most of the time
		// Specification does not require to check if they are out of cards due to multiple consecutive draws
		Player roundWinner = currentPlayer;
		int highestValue = roundWinner.getDeck().getTopCard().getCategoryValue(chosenCategory);		//**** Relies on an external unimplemented or changeable design

		int comparedPlayerValue;
		int drawValue = 0;
		
		// Iterate through each player that has a card; compare values, store highest, record any draw
		for(int i = 0; i <= numOfCompPlayers; i++) {

			if(player[i] != currentPlayer && player[i].hasCard()) {

				comparedPlayerValue = player[i].getDeck().getTopCard().getCategoryValue(chosenCategory);

				if(comparedPlayerValue > highestValue) {
					highestValue = comparedPlayerValue;
					roundWinner = player[i];
				}
				else if(comparedPlayerValue == highestValue) {
					drawValue = highestValue;
				}
				else {
					continue;	// Else compared value is < highest value so continue loop
				}

			}
		}

		// Work out round result
		if(highestValue == drawValue) {
			roundDraw();
		}
		else {
			roundWon(roundWinner);
		}
	}

	private void roundDraw() {

	}
	
	/**
	 * NOT FINISHED
	 * @param roundWinner
	 */
	private void roundWon(Player roundWinner) {
		if(roundWinner != currentPlayer) {
			currentPlayer = roundWinner;
			// Give new currentPlayer everyone's top card (including their own as it goes to bottom)
		}
		else {
			// Check if game has been won
			// Else give currentplayer everyone's top card incl their own
		}
	}

	private void endGame() {

	}

	public boolean isGameInProgress() {
		return gameInProgress;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
}
