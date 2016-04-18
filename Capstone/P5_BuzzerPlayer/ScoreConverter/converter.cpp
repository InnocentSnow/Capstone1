#include <vector>
#include <stdio.h>
#include <wchar.h>

#define EPSILON 0.001

struct Note
{
	Note(int _start, int _end, int _pitch) : start(_start), end(_end), pitch(_pitch) {}

	int start;
	int end;
	int pitch;
};

double time = 0;
std::vector<Note> v;

void writeNote(double pitch, double len)
{
	if((int)pitch) v.push_back(Note((int)time, (int)(time + len), (int)pitch));
	time += len;
}

void convert(const wchar_t *code, int tempo)
{
	const wchar_t *str = L"도레미파솔라시";
	const double dif = 1.0594630943592952645618252949463;
	const int note[] = { 262, 294, 329, 349, 392, 440, 494 };
	const double barLen = 240000 / tempo, defaultLen = barLen / 8;
	double pitch = 0, len = 0;
	int octave = 0;

	for (int i = 0; code[i]; ++i)
	{
		if (code[i] == L'u') ++octave;
		else if (code[i] == L'd') --octave;
		else if (code[i] == L'b') pitch /= dif;
		else if (code[i] == L'#') pitch *= dif;
		else if (code[i] == L'R' || code[i] == L'r')
		{
			if (EPSILON < pitch) writeNote(pitch, defaultLen);
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
				writeNote(pitch, len);
				pitch = 0;
				len = 0;
				--i;
			}
		}
		else if (L'A' <= code[i] && code[i] <= L'G')
		{
			if (EPSILON < pitch) writeNote(pitch, defaultLen);

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
					if (EPSILON < pitch) writeNote(pitch, defaultLen);

					pitch = note[j];

					if (0 <= octave) pitch *= (1 << octave);
					else pitch /= (1 << (-octave));

					break;
				}
			}
		}
	}

	if (EPSILON < pitch) writeNote(pitch, defaultLen);
}

int main()
{
	FILE *fp = fopen("input.txt", "r+");
	wchar_t buf[1024];

	int tempo;
	fscanf(fp, "%d\n", &tempo);

	while (fscanf(fp, "%S", buf) != EOF)
	{
		convert(buf, tempo);
	}

	fclose(fp);


	FILE *fout = fopen("output.txt", "w+");

	fprintf(fout, "{");

	for (int i = 0; i < v.size(); ++i)
	{
		if (i % 3 == 0) fprintf(fout, "\n\t");
		fprintf(fout, "%7d, %5d, %7d, %5d,", v[i].start, v[i].pitch, v[i].end, 0);
	}

	fprintf(fout, "\n}");
	fclose(fout);

	return 0;
}
