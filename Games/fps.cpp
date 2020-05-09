#include <ncurses.h>
#include <math.h>
#include <iostream>
using namespace std;
//anticlockwise
const char *map = "################"
                  "#              #"
                  "#              #"
                  "#              #"
                  "#              #"
                  "#              #"
                  "#              #"
                  "#              #"
                  "#              #"
                  "#              #"
                  "#              #"
                  "#              #"
                  "#              #"
                  "#              #"
                  "#              #"
                  "################";

void init()
{
  initscr();
  raw();
  noecho();
  // start_color();
  // init_pair(1, COLOR_RED, COLOR_BLACK);
  // attron(COLOR_PAIR(1));
}

int main()
{
  init();
  int player_x = 2;
  int player_y = 2;
  double player_angle = -(M_PI * 3 / 2);
  double field_of_view = M_PI / 4;
  int map_height = 16;
  int map_width = 16;
  char i = 0;
  while (1)
  {
    int row, col;
    getmaxyx(stdscr, row, col);
    char output[row * col] = {};
    output[row * col - 1] = '0';

    for (int x = 0; x < col; x++)
    {
      double angle_i = (player_angle - field_of_view / 2) + (field_of_view * x / col);
      double unit_x = cos(angle_i);
      double unit_y = sin(angle_i);
      int distance = 1;
      for (;; distance++)
      {
        int map_x = player_x + (int)(unit_x * distance);
        int map_y = player_y + (int)(unit_y * distance);
        if (map[map_y * map_width + map_x] == '#')
        {
          break;
        }
      }
      int ceiling = (row / 2) - row / distance;
      int floor = row - ceiling;
      char wall = '#';
      char ceil = '@';

      if (distance >= 0 && distance < 4)
      {
        wall = '+';
        ceil = '$';
      }
      if (distance >= 4 && distance < 8)
      {
        wall = '-';
        ceil = '%';
      }
      if (distance >= 8 && distance < 12)
      {
        wall = '*';
        ceil = '.';
      }
      if (distance >= 12 && distance < 16)
      {
        wall = '/';
        ceil = ' ';
      }

      for (int y = 0; y < row; y++)
      {
        if (y < ceiling)
        {
          output[y * col + x] = '.';
        }
        else if (y >= ceiling && y < floor)
        {
          output[y * col + x] = wall;
        }
        else
        {
          output[y * col + x] = '.';
        }
      }
    }

    printw(output);
    refresh();
    clear();
    int ch = getchar();
    if (ch == 27)
    {
      break;
    }
    if (ch == 97)
    {
      player_angle -= 0.1;
    }
    if (ch == 100)
    {
      player_angle += 0.1;
    }
  }
  endwin();
  return 0;
}
