import os
import sys
import matplotlib.pyplot as plt


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
    file_path = file_prefix + str(j) + ".xyz"

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

            states[states_key] = parsed_line[4]

        ovito_file.close()

        state_changes_over_time.append(state_changes)
        s = previously_alive + revival_count
        if s > 0:
            state_changes_percentage.append((state_changes / s) * 100)
        else:
            state_changes_percentage.append(0)

        j += 1
        file_path = file_prefix + str(j) + ".xyz"

    generate_graphs(
        output_prefix,
        state_changes_over_time,
        state_changes_percentage
    )

    return sum(state_changes_percentage), sum(state_changes_percentage) / len(state_changes_percentage)


def generate_3d_graphs(file_prefix, output_prefix):
    j = 0
    file_path = file_prefix + str(j) + ".xyz"

    state_changes_over_time = [0]
    state_changes_percentage = [0]
    states = {}
    while os.path.exists(file_path):
        ovito_file = open(file_path, "r")
        quantity = int(ovito_file.readline())  # lee la cantidad de particulas
        ovito_file.readline()  # lee un enter

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
                if j > 0 and states[states_key] != "0":
                    # Revival
                    revival_count += 1

            states[states_key] = parsed_line[5]

        ovito_file.close()

        state_changes_over_time.append(state_changes)
        s = previously_alive + revival_count
        if s > 0:
            state_changes_percentage.append((state_changes / s) * 100)
        else:
            state_changes_percentage.append(0)

        j += 1
        file_path = file_prefix + str(j) + ".xyz"

    generate_graphs(
        output_prefix,
        state_changes_over_time,
        state_changes_percentage
    )

    return sum(state_changes_percentage), sum(state_changes_percentage) / len(state_changes_percentage)


def main_graph_file(samples, is_2d, filepath):
    # Array is:
    # [ perc [ rules [ input1, input2, input3 ] ] ]
    ax = plt.subplot(111)

    ax.set_label('Output vs Input ' + ('(2D)' if is_2d else '(3D)'))
    ax.set_title('Output vs Input ' + ('(2D)' if is_2d else '(3D)'))

    ax.set_xticks([5, 21, 37, 53, 69, 85])
    ax.set_xticklabels(['5.0%', '21.0%', '37.0%', '53.0%', '69.0%', '85.0%'])
    ax.set_xlabel('Porcentaje celdas vivas inicial (input) [%]')

    ax.set_ylabel('Output')

    w = 2
    for i, n in enumerate([5, 21, 37, 53, 69, 85]):
        sample_avg_1 = sum(samples[i][0]) / 3
        sample_min_1 = min(samples[i][0])
        sample_max_1 = max(samples[i][0])

        sample_avg_2 = sum(samples[i][1]) / 3
        sample_min_2 = min(samples[i][1])
        sample_max_2 = max(samples[i][1])

        sample_avg_3 = sum(samples[i][2]) / 3
        sample_min_3 = min(samples[i][2])
        sample_max_3 = max(samples[i][2])

        if i == 0:
            ax.bar(n - w, sample_avg_1, width=w, color='#92B1B6', label="R1_" + ('2D' if is_2d else '3D'))
        else:
            ax.bar(n - w, sample_avg_1, width=w, color='#92B1B6')
        ax.errorbar(n - w, sample_avg_1, marker='_', yerr=sample_max_1 - sample_min_1, xuplims=True, xlolims=True, ecolor='#000000')

        if i == 0:
            ax.bar(n, sample_avg_2, width=w, color='#B5EAD7', label="R2_" + ('2D' if is_2d else '3D'))
        else:
            ax.bar(n, sample_avg_2, width=w, color='#B5EAD7')
        ax.errorbar(n, sample_avg_2, marker='_', yerr=sample_max_2 - sample_min_2, xuplims=True, xlolims=True, ecolor='#000000')

        if i == 0:
            ax.bar(n + w, sample_avg_3, width=w, color='#F1C5AE', label="R3_" + ('2D' if is_2d else '3D'))
        else:
            ax.bar(n + w, sample_avg_3, width=w, color='#F1C5AE')
        ax.errorbar(n + w, sample_avg_3, marker='_', yerr=sample_max_3 - sample_min_3, xuplims=True, xlolims=True, ecolor='#000000')

        ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))

    plt.tight_layout()
    plt.savefig(filepath)
    plt.clf()


def main_graph(samples_2d, samples_3d):
    main_graph_file(samples_2d, True, 'main_graph_2d_pond.png')
    main_graph_file(samples_3d, False, 'main_graph_3d_pond.png')


def main():
    if len(sys.argv) == 1:
        samples_2d = []
        samples_3d = []

        for n in [5, 21, 37, 53, 69, 85]:
            rule_samples_2d = []
            rule_samples_3d = []

            for rule in range(3):
                inner_samples_2d = []
                inner_samples_3d = []

                for i in range(3):
                    s, pond = generate_2d_graphs(os.path.join('..', 'sample_outputs', 'ovito_particles_' + str(n) + '_' + str(i) + '_R' + str(rule) + '_2D'), 'graph_' + str(n), rule)
                    inner_samples_2d.append(pond)

                    s, pond = generate_3d_graphs(os.path.join('..', 'sample_outputs', 'ovito_particles_' + str(n) + '_' + str(i)) + '_R' + str(rule) + '_3D', 'graph_' + str(n), rule)
                    inner_samples_3d.append(pond)

                rule_samples_2d.append(inner_samples_2d)
                rule_samples_3d.append(inner_samples_3d)

            samples_2d.append(rule_samples_2d)
            samples_3d.append(rule_samples_3d)

            print('Done graphs of ' + str(n) + '%')

        main_graph(samples_2d, samples_3d)
    else:
        if len(sys.argv) < 3:
            print('Arguments: 1. 2d or 3d (case sensitive). 2. filepath. If no arguments provided, it will try to parse a specific file format and folder structure')
        if sys.argv[1] == '2d':
            generate_2d_graphs(sys.argv[2], 'out_' + sys.argv[2])
        else:
            generate_3d_graphs(sys.argv[2], 'out_' + sys.argv[2])


if __name__ == '__main__':
    main()
