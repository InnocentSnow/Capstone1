#include "Tone.h"
#include "scoreA.h"
#include "scoreB.h"
#define COUNTOF(arr) (sizeof(arr) / sizeof(arr[0]))

int ledPin[]      = {13, 12, 11, 9};
int buzzerPin[]   = {8, 6};

int ledSet[2][2]  = {{0, 1}, {2, 3}};
int ledID[2]      = {0};

Tone ctone[2];

void setup()
{
  Serial.begin(9600);

  for (int i = 0; i < COUNTOF(ledPin); ++i) pinMode(ledPin[i], OUTPUT);
  for (int i = 0; i < COUNTOF(buzzerPin); ++i) pinMode(buzzerPin[i], OUTPUT);
  for (int i = 0; i < COUNTOF(buzzerPin); ++i) ctone[i].begin(buzzerPin[i]);

  play(scoreA, COUNTOF(scoreA), scoreB, COUNTOF(scoreB));
}

void loop()
{
  for (int i = 0; i < COUNTOF(ledPin); ++i)
  {
    digitalWrite(ledPin[i], HIGH);
    delay(100);
    digitalWrite(ledPin[i], LOW);
  }
}

void playNote(long &last, long start, long pitch, int id)
{
  //if(buzzerPin[id] == 6) return;

  if (last < start)
  {
    delay((start - last));
    last = start;
  }

  Serial.print(id);
  Serial.print(" ");
  Serial.print(pitch);
  Serial.print(" ");
  Serial.print(start);
  Serial.println(" ");

  if (pitch)
  {
    digitalWrite(ledPin[ledSet[id][ledID[id]++]], LOW);
    ledID[id] %= COUNTOF(ledSet[id]);
    digitalWrite(ledPin[ledSet[id][ledID[id]]], HIGH);

    ctone[id].stop();
    ctone[id].play(pitch);
  }
  else
  {
    ctone[id].stop();
  }
}

void play(const long *codeA, int lenA, const long *codeB, int lenB)
{
  long last = 0, last2 = 0;
  int i = 0, j = 0;

  while (i + 1 < lenA || j + 1 < lenB)
  {
    long startA = i + 1 < lenA ? pgm_read_dword(codeA + i) : 0;
    long pitchA = i + 1 < lenA ? pgm_read_dword(codeA + i + 1) : 0;
    long startB = j + 1 < lenB ? pgm_read_dword(codeB + j) : 0;
    long pitchB = j + 1 < lenB ? pgm_read_dword(codeB + j + 1) : 0;

    if (lenB <= j + 1 || startA <= startB)
    {
      i += 2;
      playNote(last, startA, pitchA, 0);
    }
    else
    {
      j += 2;
      playNote(last, startB, pitchB, 1);
    }
  }

  for (int i = 0; i < COUNTOF(buzzerPin); ++i) ctone[i].stop();
}


