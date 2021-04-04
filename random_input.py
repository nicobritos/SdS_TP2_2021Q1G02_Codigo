import os
import sys
import random
import math
from random import randrange
import glob
# FORMATO INPUT
#if 2D
#M
#radio1 property    x1   y1   (alive/dead (0/1))
#radio1 property    x2   y2   (alive/dead (0/1)) 
#radio1 property    x3   y3   (alive/dead (0/1)) 

#if 3D
#M
#radio1 property1   x1   y1     z1   (alive/dead (0/1))
#radio1 property2   x2   y2     z2   (alive/dead (0/1)) 
#radio1 property3   x3   y3     z3   (alive/dead (0/1)) 


def parse_args():
    # Get the total number of args passed
    total = len(sys.argv)
    if total != 5:
        print("4 argument needed, 1. 2D or 3D, 2. length of the side of the matrix in which there will be \'alive\', 3. percentage of alive particles (float), 4. qty of margin cells on the sides (ie 40)")
        quit()
    if sys.argv[1] != "2D" and sys.argv[1] != "3D":
        print("3 argument needed, 1. 2D or 3D (check it has to be in CAPS_LOCK), 2.length of the side of the matrix in which there will be \'alive\' (L^2 (or L^3) = No of cells), 3. percentage of alive particles (float)")
        quit()

    open_space = int(sys.argv[4])
    l = int(sys.argv[2])
    alive_percentage = float(sys.argv[3]) / 100

    return sys.argv[1] == "2D", l, alive_percentage, open_space


def generate_2d(file_path, L, OPEN_SPACE, alive_percentage):
    input = open(file_path, "w")
    w, h, d = L + 2 * OPEN_SPACE, L + 2 * OPEN_SPACE, L + 2 * OPEN_SPACE
    input.write(str(w))
    input.write('\n')

    alive_count = math.ceil(L ** 2 * alive_percentage)
    alive = {}
    while len(alive) < alive_count:
        alive[str(OPEN_SPACE+randrange(L)) + str(OPEN_SPACE+randrange(L))] = True

    for y in range(h):
        for x in range(w):
            # radius
            input.write("0.5")
            input.write('\t')
            # property
            input.write('1')
            input.write('\t')
            # x
            input.write(str(x))
            input.write('\t')
            # y
            input.write(str(y))
            input.write('\t')
            # alive/dead
            if str(x) + str(y) in alive:
                input.write('1')
            else:
                input.write('0')
            input.write('\n')

    input.close()


def generate_3d(file_path, L, OPEN_SPACE, alive_percentage):
    input = open(file_path, "w")
    w, h, d = L + 2 * OPEN_SPACE, L + 2 * OPEN_SPACE, L + 2 * OPEN_SPACE
    input.write(str(w))
    input.write('\n')

    alive_count = math.ceil(L ** 3 * alive_percentage)
    alive = {}
    for i in range(alive_count):
        alive[str(OPEN_SPACE+randrange(L)) + str(OPEN_SPACE+randrange(L)) + str(OPEN_SPACE + randrange(L))] = True

    for z in range(d):
        for y in range(h):
            for x in range(w):
                # radius
                input.write("0.5")
                input.write('\t')
                # property
                input.write('1')
                input.write('\t')
                # x
                input.write(str(x))
                input.write('\t')
                # y
                input.write(str(y))
                input.write('\t')
                # z
                input.write(str(z))
                input.write('\t')
                # alive/dead
                if str(x) + str(y) + str(z) in alive:
                    input.write('1')
                else:
                    input.write('0')
                input.write('\n')

    input.close()


def generate_all_files():
    for alive_percentage in range(10, 101, 10):
        for i in range(5):
            file_path_2d = './sample_inputs/input_2d_' + str(alive_percentage) + '_' + str(i) + '.txt'
            file_path_3d = './sample_inputs/input_3d_' + str(alive_percentage) + '_' + str(i) + '.txt'

            generate_2d(file_path_2d, 30, 60, alive_percentage / 100.0)
            print(file_path_2d)

            generate_3d(file_path_3d, 20, 40, alive_percentage / 100.0)
            print(file_path_3d)


def main():
    # is_2d, l, alive_percentage, open_space = parse_args()
    #
    # file_path = 'input.txt'
    #
    # if is_2d:
    #     generate_2d(file_path, l, open_space, alive_percentage)
    # else:
    #     generate_3d(file_path, l, open_space, alive_percentage)

    generate_all_files()


if __name__ == '__main__':
    main()
