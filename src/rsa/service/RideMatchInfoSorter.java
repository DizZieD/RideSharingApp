package rsa.service;

import rsa.shared.RideMatchInfo;

public interface RideMatchInfoSorter {
	java.util.Comparator<RideMatchInfo> getComparator();
}
