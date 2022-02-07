import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    /**
     * play удовлетворяет требованиям:
     * s - входной параметр
     * arr - последовательность ходов
     * возвращает 1, если на каком-то из ходов arr первый игрок первым достигает победы
     * возвращает 2, если на каком-то из ходов arr второй игрок первым достигает победы
     * возвращает 0, если arr не приводит к победе
     *
     * не предполагает случай, когда последовательность arr невозможна
     *
     * частным решением проблемы может быть подгон arr под возможную комбинацию(в случае, если вместо одного хода можно однозначно выбрать другой)
     *
     * частным решением проблемы может быть ограничение перебора внутри must и have
     */


    // пример play для условия:

    /*
    Два игрока, Петя и Ваня, играют в следующую игру. Перед игроками лежат две кучи камней. Игроки ходят по очереди, первый ход делает Петя.
    За один ход игрок может добавить в одну из куч один камень или увеличить количество камней в куче в два раза. Чтобы делать ходы, у каждого
    игрока есть неограниченное количество камней. Игра завершается в тот момент, когда суммарное количество камней в кучах становится не менее 77.
    Победителем считается игрок, сделавший последний ход, т. е. первым получивший позицию, в которой в кучах будет 77 или больше камней.

    В начальный момент в первой куче было 7 камней, во второй куче – S камней, 1 ≤ S ≤ 69.
    Будем говорить, что игрок имеет выигрышную стратегию, если он может выиграть при любых ходах противника.
    Известно, что Ваня выиграл своим первым ходом после неудачного первого хода Пети. Назовите минимальное значение S, при котором это возможно.
     */
    private static int play(int s, List<Integer> arr) {
        int a = 7, b = s;

        for(int i = 0; i < arr.size(); i++){
            //делаем ход
            switch (arr.get(i)){
                //первый тип ходов - 1 куча +1
                case 0:
                    a+=1;
                    break;

                //второй тип ходов - 1 куча *2
                case 1:
                    a*=2;
                    break;

                //третий тип ходов - 2 куча +1
                case 2:
                    b+=1;
                    break;

                //четвертый тип ходов - 2 куча *2
                case 3:
                    b*=2;
                    break;
            }

            //если кто-то победил, возвращаем результат
            if(a+b>=77){
                return i%2+1;
            }
        }

        //никто не победил
        return 0;
    }
    public static List<Integer> moves = Arrays.asList(0, 1, 2, 3); //Массив возможных типов ходов

    /**
     * на ход протагониста
     * (длина массива должна быть четной)
     * (имеется стратегия игры при сделанных ходах arr)
     * (т.е. найдется ход, который при уже сделанных ходах arr обязательно приведет к победе)
     * depth равен максимальному количеству рассматриваемых ходов(и протагониста, и антагониста)
     */
    static boolean have(int s, List<Integer> arr, int depth) {
        int res = play(s, arr);
        if (res != 0) {
            return res == arr.size() % 2 + 1;
        }

        if (depth == 0) {
            return false;
        }
        boolean f = false;
        for (int i : moves)      //перебор всех ходов протагониста(вместо moves может быть массив типов ходов)
        {
            List<Integer> arr1 = new ArrayList<>(arr);
            arr1.add(i);
            f = f || must(s, arr1, depth - 1);
        }
        return f;
    }

    /**
     * на ход антагониста
     * (длина массива должна быть нечетной)
     * (может выиграть при сделанных ходах arr)
     * (т.е. на любой ход при уже сделанных ходах arr найдется стратегия)
     * depth равен максимальному количеству рассматриваемых ходов(и протагониста, и антагониста)
     */
    static boolean must(int s, List<Integer> arr, int depth) {
        int res = play(s, arr);
        if (res != 0) {
            return res != arr.size() % 2 + 1;
        }
        if (depth == 0) {
            return false;
        }
        boolean f = true;
        for (int i : moves)      //перебор всех ходов антагониста(вместо range(4) может быть массив типов ходов)
        {
            List<Integer> arr1 = new ArrayList<>(arr);
            arr1.add(i);
            f = f && have(s, arr1, depth - 1);
        }
        return f;
    }


    public static void main(String[] args) {
        int s = 10;
        /* Пример употребления */
        //Arrays.asList(a, b, c, d, ...) создает List с элементами a, b, c, d, ...
        //Arrays.asList() создает List без элементов

        //первый игрок имеет стратегию, позволяющую выиграть за 1 или 2 его хода при любом ходе второго игрока
        System.out.println(have(s, Arrays.asList(), 3));

        //первый игрок не имеет стратегии, позволяющей выиграть за 1 ход
        System.out.println(!have(s, Arrays.asList(), 1));

        //второй игрок может выиграть не более чем за 2 своих хода при любых первых двух ходах первого игрока(т.е. имеет стратегию)
        System.out.println(must(s, Arrays.asList(), 4));

        //первый игрок имеет стратегию, позволяющую выиграть при сделанной последовательности ходов 0,0,1,1
        System.out.println(have(s, Arrays.asList(0, 0, 1, 1), 100000));

        //Предупреждение: алгоритм работает за O(x^n), где x-кол-во возможных действий за один ход, n - кол-во ходов
        //Оценка: 1 секунда: n=24 x=2
        //Оценка: 10 секунд: n=28 x=2
        //Оценка: 100 секунд: n=31 x=2
    }
}
