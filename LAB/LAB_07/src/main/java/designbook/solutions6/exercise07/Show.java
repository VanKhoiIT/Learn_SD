package designbook.solutions6.exercise07;

import java.util.Collections;
import java.util.Iterator;

/**
 * Exercise 6.7 - Giao diện Show ĐÃ ĐƯỢC SỬA ĐỔI:
 * Kế thừa trực tiếp Iterable<Show> theo hướng tiếp cận Trong suốt (Transparency Approach).
 * Cung cấp Default Empty Iterator cho các nút lá (Movie, Concert) để không vi phạm LSP.
 */
public interface Show extends Iterable<Show> {

	/**
	 * @return A description of the show.
	 */
	String description();

	/**
	 * @return The running time of the show, in minutes.
	 */
	int runningTime();

	/**
	 * @return A deep copy of the show.
	 */
	Show copy();

	/**
	 * Default iterator cho các nút lá đơn lẻ.
	 * Vòng lặp for-each trên nút lá sẽ kết thúc ngay (0 lần lặp) mà không gây lỗi.
	 */
	@Override
	default Iterator<Show> iterator() {
		return Collections.emptyIterator();
	}
}

