#define COUNTOF(arr) (sizeof(arr) / sizeof(arr[0]))
int ledPin[] = {13, 12, 11, 9};
int buzzerPin = 8;

// the setup function runs once when you press reset or power the board
void setup() {
  for(int i = 0; i < COUNTOF(ledPin); ++i) pinMode(ledPin[i], OUTPUT);
  pinMode(buzzerPin, OUTPUT);

  const wchar_t *t1 = L"dB8uF#8B8F#8dG8uD8G8D8";
  const wchar_t *t2 = L"dA8uE8A8E8D8A8uD8dA8";
  const wchar_t *t3 = L"dA8uE8A8E8D8A8uA16uD16E16F#16";
  const wchar_t *t4 = L"E8D16D16+8R4dA16uD16E16F#16";
  const wchar_t *t5 = L"E8D16E16+16F#8F#16+4dA16uD16E16F#16";
  const wchar_t *t6 = L"E8D16D16+4R4dA16uD16E16F#16";
  const wchar_t *t7 = L"E8D16E16+16A8F#16+4F#8G8";
  const wchar_t *t8 = L"A8A16A16+8A8A8F#16D16+8F#16G16";
  const wchar_t *t9 = L"A8A16A16+8A8A8F#16D16+8D16E16";
  const wchar_t *t10 = L"F#8F#16F#16+8F#8F#8B8B16F#16E16D8";
  const wchar_t *t11 = L"E4E8dB16B16uE8C#8dA16uD16E16F#16";
  const wchar_t *t12 = L"F#8F#16F#16+8F#8F#4B24F#24E24D16dB16";
  const wchar_t *t13 = L"uD4D8dA8F#2";
  const wchar_t *t14 = L"F#4D8F#8E2";
  const wchar_t *t15 = L"A4A8E16D16A4A8E16D16";
  const wchar_t *t16 = L"A4A8E16D16E2";
  const wchar_t *t17 = L"A16dA16uD16E16A16E16D16dA16uA16dA16uD16E16A16E16D16dA16";
  const wchar_t *t18 = L"uD16dD16G16A16uD16dA16G16D16uD16dD16G16A16uD16dA16G16D16";
  const wchar_t *t19 = L"F#8F#16F#16+8F#8F#8B8B24F#24E24D8";
  const wchar_t *t20 = L"uD4uD4C#4+8dA8";
  const wchar_t *t21 = L"F#4dD8F#8E4A16uD16E16F#16";
  const wchar_t *t22 = L"E8D16D16uD4C#4+8dA8";
  const wchar_t *t23 = L"B4ddB16uC#16D16E16F#16G16E8A16uD16E16F#16";
  const wchar_t *t24 = L"D4uD4C#4+8dA8F#4dD8F#8";
  const wchar_t *t25 = L"E4A16uD16E16F#16";
  const wchar_t *t26 = L"E8D16D16+4dB8+16A16+8G8";
  const wchar_t *t27 = L"F#1";
  const wchar_t *u = L"u";
  const wchar_t *d = L"d";

  const wchar_t *notes[] = {
   d, t1, t2, t1, t3, t4, t5, t6, t7, t8, t9, t10, t11, t4, t5, t6, t7, t8, t9, t12, t13, t14, t15, t16,
   u, t17, u, t17, u, t17, u, t17, u, t18, t18, t18, t18, t17, u, t17, u, t17, u, t17,
   u, t8, t8, t19, t11, t6, t5, t6, t7, t8, t9, t12,
   t20, t21, t22, t23, t24, t25, t26, t27
  };

  int tempo = 120;

  for(int i = 0; i < COUNTOF(notes); ++i)
  {
    play(notes[i], tempo);
  }


/*     BECAUSE OF MEMORY
 *      
 *      ORIGINAL : 
  L"ddB8uF#8B8F#8dG8uD8G8D8 dA8uE8A8E8D8A8uD8dA8 dB8uF#8B8F#8dG8uD8G8D8 dA8uE8A8E8D8A8uA16uD16E16F#16 "
  L"E8D16D16+8R4dA16uD16E16F#16 E8D16E16+16F#8F#16+4dA16uD16E16F#16 E8D16D16+4R4dA16uD16E16F#16 E8D16E16+16A8F#16+4F#8G8 "
  L"A8A16A16+8A8A8F#16D16+8F#16G16 A8A16A16+8A8A8F#16D16+8D16E16 F#8F#16F#16+8F#8F#8B8B16F#16E16D8 E4E8dB16B16uE8C#8dA16uD16E16F#16"

  L"E8D16D16+8R4dA16uD16E16F#16 E8D16E16+16F#8F#16+4dA16uD16E16F#16 E8D16D16+4R4dA16uD16E16F#16 E8D16E16+16A8F#16+4F#8G8 "
  L"A8A16A16+8A8A8F#16D16+8F#16G16 A8A16A16+8A8A8F#16D16+8D16E16"

  L"F#8F#16F#16+8F#8F#4B24F#24E24D16dB16 uD4D8dA8F#2 F#4D8F#8E2 A4A8E16D16A4A8E16D16 "
  L"A4A8E16D16E2"

  L"uA16dA16uD16E16A16E16D16dA16uA16dA16uD16E16A16E16D16dA16 uA16dA16uD16E16A16E16D16dA16uA16dA16uD16E16A16E16D16dA16 "
  L"uA16dA16uD16E16A16E16D16dA16uA16dA16uD16E16A16E16D16dA16 uA16dA16uD16E16A16E16D16dA16uA16dA16uD16E16A16E16D16dA16 "
  L"uuD16dD16G16A16uD16dA16G16D16uD16dD16G16A16uD16dA16G16D16 uD16dD16G16A16uD16dA16G16D16uD16dD16G16A16uD16dA16G16D16 "
  L"uD16dD16G16A16uD16dA16G16D16uD16dD16G16A16uD16dA16G16D16 uD16dD16G16A16uD16dA16G16D16uD16dD16G16A16uD16dA16G16D16 "
  L"A16dA16uD16E16A16E16D16dA16uA16dA16uD16E16A16E16D16dA16 uA16dA16uD16E16A16E16D16dA16uA16dA16uD16E16A16E16D16dA16 "
  L"uA16dA16uD16E16A16E16D16dA16uA16dA16uD16E16A16E16D16dA16 uA16dA16uD16E16A16E16D16dA16uA16dA16uD16E16A16E16D16dA16 "

  L"uA8A16A16+8A8A8F#16D16+8F#16G16 A8A16A16+8A8A8F#16D16+8F#16G16 F#8F#16F#16+8F#8F#8B8B24F#24E24D8 "
  L"E4E8dB16B16uE8C#8dA16uD16E16F#16 E8D16D16+4R4dA16uD16E16F#16 E8D16E16+16F#8F#16+4dA16uD16E16F#16 E8D16D16+4R4dA16uD16E16F#16"
  L"E8D16E16+16A8F#16+4F#8G8 A8A16A16+8A8A8F#16D16+8F#16G16 A8A16A16+8A8A8F#16D16+8D16E16 F#8F#16F#16+8F#8F#4B24F#24E24D16dB16 "
  
  L"uD4uD4C#4+8dA8 F#4dD8F#8E4A16uD16E16F#16 E8D16D16uD4C#4+8dA8 B4ddB16uC#16D16E16F#16G16E8A16uD16E16F#16 "
  L"D4uD4C#4+8dA8F#4dD8F#8 E4A16uD16E16F#16 E8D16D16+4dB8+16A16+8G8 F#1"
 */
  
}

// the loop function runs over and over again forever
void loop() {
  for(int i = 0; i < COUNTOF(ledPin); ++i)
  {
	digitalWrite(ledPin[i], HIGH);
	delay(100);
	digitalWrite(ledPin[i], LOW);
  }
}

void playNote(double pitch, double len)
{
  static int ledID = 0;

  digitalWrite(ledPin[ledID], HIGH);
  tone(buzzerPin, (int)pitch, (int)len);
  delay(len + 10);
  digitalWrite(ledPin[ledID], LOW);
  ledID = (ledID + 1) % COUNTOF(ledPin);
}

void play(const wchar_t *code, int tempo)
{
  const wchar_t *str = L"도레미파솔라시";
  const double dif = 1.0594630943592952645618252949463;
  const int note[] = {262, 294, 329, 349, 392, 440, 494};
  const double barLen = 240000 / tempo, defaultLen = barLen / 8;
  double pitch = 0, len = 0;
  static int octave = 0;

  for (int i = 0; code[i]; ++i)
  {
    if (code[i] == L'u') ++octave;
    else if (code[i] == L'd') --octave;
    else if (code[i] == L'b') pitch /= dif;
    else if (code[i] == L'#') pitch *= dif;
    else if (code[i] == L'R' || code[i] == L'r')
    {
      if (pitch) playNote(pitch, defaultLen);
      pitch = 0;
    }
    else if (L'0' <= code[i] && code[i] <= L'9')
    {
      int type = 0;

      while (code[i])
      {
        if (L'0' <= code[i] && code[i] <= L'9')
        {
          type *= 10;
          type += code[i++] - L'0';
        }
        else break;
      }

      len += barLen / type;

      if (code[i] != '+')
      {
        playNote(pitch, len);
        pitch = 0;
        len = 0;
        --i;
      }
    }
    else if (L'A' <= code[i] && code[i] <= L'G')
    {
      if (pitch) playNote(pitch, defaultLen);

      int t = code[i] - L'C';
      if (t < 0) t += 7;

      pitch = note[t];

      if (0 <= octave) pitch *= (1 << octave);
      else pitch /= (1 << (-octave));
    }
    else
    {
      for (int j = 0; j < 7; ++j)
      {
        if (code[i] == str[j])
        {
          if (pitch) playNote(pitch, defaultLen);

          pitch = note[j];

          if (0 <= octave) pitch *= (1 << octave);
          else pitch /= (1 << (-octave));

          break;
        }
      }
    }
  }

  if (pitch) playNote(pitch, defaultLen);
}

