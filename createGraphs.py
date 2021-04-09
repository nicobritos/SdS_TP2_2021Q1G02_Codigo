import os
import sys
from textwrap import wrap

import matplotlib.pyplot as plt
import numpy as np


L_2D = 60
L_3D = 20
L_2D_MIDDLE = L_2D / 2
L_3D_MIDDLE = L_3D / 2


def wrap_title(title):
    return "\n".join(wrap(title, 60))


def generate_graphs(file_prefix, state_changes, state_changes_percentage):
    plt.plot(state_changes)
    plt.xlabel('Tiempo')
    plt.ylabel('Cantidad de cambios de estado')
    plt.title('Cantidad de cambios de estado vs Tiempo')
    plt.savefig(file_prefix + '_chaos.png')
    plt.clf()

    plt.plot(state_changes_percentage)
    plt.xlabel('Tiempo')
    plt.ylabel('% Cantidad de cambios de estado')
    plt.title('% Cantidad de cambios de estado vs Tiempo')
    plt.savefig(file_prefix + '_chaos2.png')
    plt.clf()


def generate_2d_graphs(file_prefix, output_prefix):
    j = 0
    file_path = file_prefix + '\\' + str(j) + ".xyz"

    max_radius_over_time = []
    alive_count_over_time = []
    state_changes_over_time = [0]
    state_changes_percentage = [0]
    states = {}
    while os.path.exists(file_path):
        ovito_file = open(file_path, "r")
        quantity = int(ovito_file.readline())  # lee la cantidad de particulas
        ovito_file.readline()  # lee un enter

        alive_count = 0
        state_changes = 0
        revival_count = 0
        previously_alive = 0
        max_distance = 0
        for k in range(quantity):
            parsed_line = ovito_file.readline().split()

            states_key = parsed_line[2] + parsed_line[3]

            if j > 0:
                if states[states_key] != parsed_line[4]:
                    state_changes += 1
                if states[states_key] == "0":
                    previously_alive += 1

            if parsed_line[4] == "0":
                alive_count += 1
                if j > 0 and states[states_key] != "0":
                    # Revival
                    revival_count += 1

                x = float(parsed_line[2])
                y = float(parsed_line[3])

                distance_x = abs(L_2D_MIDDLE - x)
                distance_y = abs(L_2D_MIDDLE - y)

                distance = np.sqrt(distance_x ** 2 + distance_y ** 2)
                if max_distance < distance:
                    max_distance = distance

            states[states_key] = parsed_line[4]

        ovito_file.close()

        max_radius_over_time.append(max_distance)
        alive_count_over_time.append(alive_count)
        state_changes_over_time.append(state_changes)
        s = previously_alive + revival_count
        if s > 0:
            state_changes_percentage.append((state_changes / s) * 100)
        else:
            state_changes_percentage.append(0)

        j += 1
        file_path = file_prefix + '\\' + str(j) + ".xyz"

    generate_graphs(
        output_prefix,
        state_changes_over_time,
        state_changes_percentage
    )

    if len(state_changes_percentage) - 1 > 0:
        state_changes_avg = sum(state_changes_percentage[1:]) / (len(state_changes_percentage) - 1)
    else:
        state_changes_avg = 0
    return state_changes_avg, alive_count_over_time, max_radius_over_time


def generate_3d_graphs(file_prefix, output_prefix):
    j = 0
    file_path = file_prefix + '\\' + str(j) + ".xyz"

    max_radius_over_time = []
    alive_count_over_time = []
    state_changes_over_time = [0]
    state_changes_percentage = [0]
    states = {}
    max_distance = 0
    while os.path.exists(file_path):
        ovito_file = open(file_path, "r")
        quantity = int(ovito_file.readline())  # lee la cantidad de particulas
        ovito_file.readline()  # lee un enter

        alive_count = 0
        state_changes = 0
        revival_count = 0
        previously_alive = 0
        for k in range(quantity):
            parsed_line = ovito_file.readline().split()

            states_key = parsed_line[2] + parsed_line[3] + parsed_line[4]

            if j > 0:
                if states[states_key] != parsed_line[5]:
                    state_changes += 1
                if states[states_key] == "0":
                    previously_alive += 1

            if parsed_line[5] == "0":
                alive_count += 1
                if j > 0 and states[states_key] != "0":
                    # Revival
                    revival_count += 1

                x = float(parsed_line[2])
                y = float(parsed_line[3])
                z = float(parsed_line[0])

                distance_x = abs(L_3D_MIDDLE - x)
                distance_y = abs(L_3D_MIDDLE - y)
                distance_z = abs(L_3D_MIDDLE - z)

                distance = np.sqrt(distance_x ** 2 + distance_y ** 2 + distance_z ** 2)
                if max_distance < distance:
                    max_distance = distance

            states[states_key] = parsed_line[5]

        ovito_file.close()

        max_radius_over_time.append(max_distance)
        alive_count_over_time.append(alive_count)
        state_changes_over_time.append(state_changes)
        s = previously_alive + revival_count
        if s > 0:
            state_changes_percentage.append((state_changes / s) * 100)
        else:
            state_changes_percentage.append(0)

        j += 1
        file_path = file_prefix + '\\' + str(j) + ".xyz"

    generate_graphs(
        output_prefix,
        state_changes_over_time,
        state_changes_percentage
    )

    if len(state_changes_percentage) - 1 > 0:
        state_changes_avg = sum(state_changes_percentage[1:]) / (len(state_changes_percentage) - 1)
    else:
        state_changes_avg = 0
    return state_changes_avg, alive_count_over_time, max_radius_over_time


def graph_all_output_vs_input(samples, is_2d):
    x = [5, 21, 37, 53, 69, 85]

    plt.plot(x, [samples[i]['pond']['list'][0] for i, n in enumerate(x)], label="R1_" + ('2D' if is_2d else '3D'))
    plt.plot(x, [samples[i]['pond']['list'][1] for i, n in enumerate(x)], label="R2_" + ('2D' if is_2d else '3D'))
    plt.plot(x, [samples[i]['pond']['list'][2] for i, n in enumerate(x)], label="R3_" + ('2D' if is_2d else '3D'))

    for i, n in enumerate(x):
        plt.errorbar(n, samples[i]['pond']['list'][0], marker='_', yerr=samples[i]['pond']['error'][0], xuplims=True, xlolims=True, ecolor='#000000')
        plt.errorbar(n, samples[i]['pond']['list'][1], marker='_', yerr=samples[i]['pond']['error'][1], xuplims=True, xlolims=True, ecolor='#000000')
        plt.errorbar(n, samples[i]['pond']['list'][2], marker='_', yerr=samples[i]['pond']['error'][2], xuplims=True, xlolims=True, ecolor='#000000')

    plt.xlabel('Porcentaje celdas vivas inicial (input) [%]')
    plt.ylabel('Output')
    plt.xticks(x, [str(n) + '.0%' for n in x])
    plt.yticks(list(range(0, 101, 10)))
    plt.title('Output vs Input ' + ('(2D)' if is_2d else '(3D)'))
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.savefig('all_output_graph_' + ('2D' if is_2d else '3D') + '.png')
    plt.clf()


def graph_output_vs_input(samples, is_2d, rule):
    x = [5, 21, 37, 53, 69, 85]

    plt.plot(x, [samples[i]['pond']['list'][rule] for i, n in enumerate(x)], label="R" + str(rule + 1) + "_" + ('2D' if is_2d else '3D'))

    for i, n in enumerate(x):
        plt.errorbar(n, samples[i]['pond']['list'][rule], marker='_', yerr=samples[i]['pond']['error'][rule], xuplims=True, xlolims=True, ecolor='#000000')

    plt.xlabel('Porcentaje celdas vivas inicial (input) [%]')
    plt.ylabel('Output')
    plt.xticks(x, [str(n) + '.0%' for n in x])
    plt.yticks(list(range(0, 101, 10)))
    plt.title(wrap_title('Output vs Input R' + str(rule + 1) + '_' + ('2D' if is_2d else '3D')))
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.savefig('output_R' + str(rule + 1) + ('_2D' if is_2d else '_3D') + '_graph.png')
    plt.clf()


def graph_all_alive_cells_vs_time(samples, is_2d, rule):
    x = [5, 21, 37, 53, 69, 85]

    for i, n in enumerate(x):
        plt.plot(samples[i]['alive']['list'][rule], label=str(n) + ".0%")

    plt.xlabel('Tiempo')
    plt.ylabel('Cantidad de celdas vivas')
    plt.title(wrap_title('Cantidad de celdas vivas vs Tiempo ' + ('2D' if is_2d else '3D')))
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.savefig('all_alive_cells_vs_time_R' + str(rule + 1) + '_' + ('2D' if is_2d else '3D') + '_graph.png')
    plt.clf()


def graph_alive_cells_vs_time(samples, is_2d, rule, n):
    plt.plot(samples['alive']['list'][rule], label=str(n) + ".0%")

    plt.xlabel('Tiempo')
    plt.ylabel('Cantidad de celdas vivas')
    plt.title(wrap_title('Cantidad de celdas vivas vs Tiempo con porcentaje celdas vivas inicial (input) de ' + str(n) + ".0% " + ('(2D)' if is_2d else '(3D)')))
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.savefig('alive_cells_vs_time_' + str(n) + '_R' + str(rule + 1) + '_' + ('2D' if is_2d else '3D') + '_graph.png')
    plt.clf()


def graph_all_max_radius_vs_time(samples, is_2d, rule):
    x = [5, 21, 37, 53, 69, 85]

    for i, n in enumerate(x):
        plt.plot(samples[i]['max_radius']['list'][rule], label=str(n) + ".0%")

    plt.xlabel('Tiempo')
    plt.ylabel('Maximo radio del patron')
    plt.title(wrap_title('Maximo radio del patron vs Tiempo ' + ('2D' if is_2d else '3D')))
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.savefig('all_max_radius_vs_time_R' + str(rule + 1) + '_' + ('2D' if is_2d else '3D') + '_graph.png')
    plt.clf()


def graph_max_radius_vs_time(samples, is_2d, rule, n):
    plt.plot(samples['max_radius']['list'][rule], label=str(n) + ".0%")

    plt.xlabel('Tiempo')
    plt.ylabel('Maximo radio del patron')
    plt.title(wrap_title('Maximo radio del patron vs Tiempo con porcentaje celdas vivas inicial (input) de ' + str(n) + ".0% " + ('(2D)' if is_2d else '(3D)')))
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.savefig('max_radius_vs_time_' + str(n) + '_R' + str(rule + 1) + '_' + ('2D' if is_2d else '3D') + '_graph.png')
    plt.clf()


def main_graph_file(samples, is_2d):
    graph_all_output_vs_input(samples, is_2d)

    x = [5, 21, 37, 53, 69, 85]
    for rule in range(3):
        graph_output_vs_input(samples, is_2d, rule)

        graph_all_alive_cells_vs_time(samples, is_2d, rule)
        for i, n in enumerate(x):
            graph_alive_cells_vs_time(samples[i], is_2d, rule, n)

        graph_all_max_radius_vs_time(samples, is_2d, rule)
        for i, n in enumerate(x):
            graph_max_radius_vs_time(samples[i], is_2d, rule, n)


def get_avg_std(pond_runs, alive_runs, max_radius_runs):
    max_max_radius_runs = max([len(run) for run in max_radius_runs])
    max_alive_results = max([len(run) for run in alive_runs])

    results = {
        'pond': {
            'value': 0,
            'error': 0
        },
        'alive': {
            'list': [],
            'error': []
        },
        'max_radius': {
            'list': [],
            'error': []
        },
    }

    results['pond']['value'] = np.average(pond_runs)
    results['pond']['error'] = np.std(pond_runs)

    for i, p in enumerate(range(max_alive_results)):
        alive_run_results = []
        for run in alive_runs:
            if i < len(run):
                alive_run_results.append(run[i])
            else:
                alive_run_results.append(0)

        results['alive']['list'].append(np.average(alive_run_results))
        results['alive']['error'].append(np.std(alive_run_results))

    for i, p in enumerate(range(max_max_radius_runs)):
        max_radius_run_results = []
        for run in max_radius_runs:
            if i < len(run):
                max_radius_run_results.append(run[i])
            else:
                max_radius_run_results.append(0)

        results['max_radius']['list'].append(np.average(max_radius_run_results))
        results['max_radius']['error'].append(np.std(max_radius_run_results))

    return results


def main():
    if len(sys.argv) == 1:
        samples_2d = []
        samples_3d = []

        for n in [5, 21, 37, 53, 69, 85]:
            rule_samples_2d = {
                'pond': {
                    'list': [],
                    'error': []
                },
                'alive': {
                    'list': [],
                    'error': []
                },
                'max_radius': {
                    'list': [],
                    'error': []
                },
            }
            rule_samples_3d = {
                'pond': {
                    'list': [],
                    'error': []
                },
                'alive': {
                    'list': [],
                    'error': []
                },
                'max_radius': {
                    'list': [],
                    'error': []
                },
            }

            for rule in range(3):
                inner_samples_2d = {
                    'pond': [],
                    'alive': [],
                    'max_radius': [],
                }
                inner_samples_3d = {
                    'pond': [],
                    'alive': [],
                    'max_radius': [],
                }

                for i in range(3):
                    pond, alive, max_radius = generate_2d_graphs(os.path.join('..', 'sample_outputs', 'ovito_particles_' + str(n) + '_' + str(i) + '_R' + str(rule) + '_2D'), 'graph_' + str(i) + '_' + str(n) + '_R' + str(rule + 1) + '_2D')
                    inner_samples_2d['pond'].append(pond)
                    inner_samples_2d['alive'].append(alive)
                    inner_samples_2d['max_radius'].append(max_radius)

                    pond, alive, max_radius = generate_3d_graphs(os.path.join('..', 'sample_outputs', 'ovito_particles_' + str(n) + '_' + str(i)) + '_R' + str(rule) + '_3D', 'graph_' + str(i) + '_' + str(n) + '_R' + str(rule + 1) + '_3D')
                    inner_samples_3d['pond'].append(pond)
                    inner_samples_3d['alive'].append(alive)
                    inner_samples_3d['max_radius'].append(max_radius)

                result = get_avg_std(inner_samples_2d['pond'], inner_samples_2d['alive'], inner_samples_2d['max_radius'])
                rule_samples_2d['pond']['list'].append(result['pond']['value'])
                rule_samples_2d['pond']['error'].append(result['pond']['error'])
                rule_samples_2d['alive']['list'].append(result['alive']['list'])
                rule_samples_2d['alive']['error'].append(result['alive']['error'])
                rule_samples_2d['max_radius']['list'].append(result['max_radius']['list'])
                rule_samples_2d['max_radius']['error'].append(result['max_radius']['error'])

                result = get_avg_std(inner_samples_3d['pond'], inner_samples_3d['alive'], inner_samples_3d['max_radius'])
                rule_samples_3d['pond']['list'].append(result['pond']['value'])
                rule_samples_3d['pond']['error'].append(result['pond']['error'])
                rule_samples_3d['alive']['list'].append(result['alive']['list'])
                rule_samples_3d['alive']['error'].append(result['alive']['error'])
                rule_samples_3d['max_radius']['list'].append(result['max_radius']['list'])
                rule_samples_3d['max_radius']['error'].append(result['max_radius']['error'])

            samples_2d.append(rule_samples_2d)
            samples_3d.append(rule_samples_3d)

            print('Done graphs of ' + str(n) + '%')

        main_graph_file(samples_2d, True)
        main_graph_file(samples_3d, False)
    else:
        if len(sys.argv) < 3:
            print('Arguments: 1. 2d or 3d (case sensitive). 2. filepath. If no arguments provided, it will try to parse a specific file format and folder structure')
        if sys.argv[1] == '2d':
            generate_2d_graphs(sys.argv[2], 'out_' + sys.argv[2])
        else:
            generate_3d_graphs(sys.argv[2], 'out_' + sys.argv[2])


if __name__ == '__main__':
    main()
