package designbook.solutions6.exercise07;

import java.util.Objects;

/**
 * Leaf node Concert trong Exercise 6.7.
 * Cài đặt giao diện Show (đã sửa đổi kế thừa Iterable<Show>).
 */
public class Concert implements Show {

	private final String aTitle;
	private final String aPerformer;
	private final int aRunningTime;

	public Concert(String pTitle, String pPerformer, int pTime) {
		assert pTitle != null && pPerformer != null && pTime >= 0;
		aTitle = pTitle;
		aPerformer = pPerformer;
		aRunningTime = pTime;
	}

	public Concert(Concert pConcert) {
		this(pConcert.aTitle, pConcert.aPerformer, pConcert.aRunningTime);
	}

	public String title() {
		return aTitle;
	}

	public String performer() {
		return aPerformer;
	}

	@Override
	public String description() {
		return String.format("%s by %s", aTitle, aPerformer);
	}

	@Override
	public int runningTime() {
		return aRunningTime;
	}

	@Override
	public Show copy() {
		return new Concert(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(aPerformer, aRunningTime, aTitle);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Concert other = (Concert) obj;
		return Objects.equals(aPerformer, other.aPerformer) && aRunningTime == other.aRunningTime &&
				Objects.equals(aTitle, other.aTitle);
	}

	@Override
	public String toString() {
		return description();
	}
}

