import sys

#taking inputs
word = sys.argv[1]
letters = sys.argv[2]

#making the inputs usuable for me
lettersU = letters.split(",")
wordU = list(word)

length = len(wordU)

#makining the closed list
wordGuessed = list()
for x in wordU:
    wordGuessed.append("-")

#for opening the letter which is known
def fillGuessedWord(letter):
    for i in range(length):
        if wordU[i] == letter:
            wordGuessed[i] = letter

#initialize game
mode = "IN"
remainingGuess = 5
inLetters = list()
outLetters = list()
gameWon = False

print("You have " + str(remainingGuess) + " guesses left \n" , wordGuessed)
print("--------------------------------------------")

#for loop for input letters
for letter in lettersU :

#checking the mode to chose which path to follow
    if mode == "IN":

        if letter in inLetters:
            remainingGuess -= 1
            print("Guessed word: " + letter + " is used in IN mode. The game turned into OUT mode")
            mode = "OUT"

        elif letter in wordU:
            inLetters.append(letter)
            print("Guessed word: " + letter + " You are in IN mode")
            fillGuessedWord(letter)
            if wordGuessed == wordU:
                gameWon = True

        else:
            inLetters.append(letter)
            remainingGuess -= 1
            mode = "OUT"
            print("Guessed word: " + letter + " The game turned into OUT mode")

    else:

        if letter in outLetters:
            remainingGuess -= 1
            print("Guessed word: " + letter + " is used in OUT mode. The game is in OUT mode.")

        elif letter in wordU:
            outLetters.append(letter)
            remainingGuess -= 1
            print("Guessed word: " + letter + " You are in OUT mode")

        else:
            outLetters.append(letter)
            mode = "IN"
            print("Guessed word: " + letter + " The game turned into IN mode")

#the final output for each step
    print(" You have " + str(remainingGuess) + " guesses left \n", wordGuessed)
    print("--------------------------------------------")

#terminating the for loop
    if gameWon == True or remainingGuess == 0:
        break

print("")

#printing the result
if gameWon == True:
    print("You won the game")
else:
    if remainingGuess == 0:
        print("You lost the game")
    else:
        print("You finished all letters")
        print("You lost the game")