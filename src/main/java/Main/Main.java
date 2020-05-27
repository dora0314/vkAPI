package Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
            throws IOException, AccesClosedException, PrivateProfileException, TooManyRequestsException, InterruptedException {
        System.out.println("You can find out the list of your friends, list of some other user's friends," +
                " your and other user's mutual friends list and people with the most count of common friends.")

        Scanner scanner = new Scanner(System.in);
        System.out.println("Write your User Token");
        String userToken = scanner.nextLine();//        "ee460cc8652d97a6b60b99fb3228cbd53255e8157970eeb52d16c885972ab44b5b17eb6503e4adbaaac3f"
        System.out.println("Write your User ID");
        Integer userid = scanner.nextInt(); //77201136
        System.out.println("Write other person User ID");
        Integer otherUserid = scanner.nextInt();
        System.out.println("Write how many requests need to be send:");
        int requestsCount = scanner.nextInt();
        System.out.println("Write timeout of each request (min)");
        int timeOut = scanner.nextInt();//900
        scanner.close();

        ArrayList<Integer> friendsIDList = Worker.getFriendsList(userid + "", Worker.serviceToken);
        System.out.println("************************************************************");
        System.out.println("Your friends count: " + friendsIDList.size());
        System.out.println("Your friends list: " + friendsIDList);
        System.out.println(" ");

        ArrayList<Integer> otherFriendsIDList = Worker.getFriendsList(otherUserid + "", Worker.serviceToken);
        System.out.println("************************************************************");
        System.out.println("Other user's friends count: " + otherFriendsIDList.size());
        System.out.println("Other user's friends list: " + otherFriendsIDListriendsIDList);
        System.out.println(" ");

        ArrayList<Integer> mutualFriendsIDList = Worker.getMutalFriendsList(userid, otherUserid, userToken);
        System.out.println("************************************************************");
        System.out.println("Mutual friends count: " + mutualFriendsIDList.size());
        System.out.println("Mutual friends list: " + mutualFriendsIDList);
        System.out.println(" ");

        ArrayList<Integer> friendsOfFriendsIds = new ArrayList<Integer>();
        for (Integer id : friendsIDList) {
            try {
                ArrayList<Integer> tempList = Worker.getFriendsList(id + "", Worker.serviceToken);
                if (tempList.size() <= 250)
                    for (Integer id2 : tempList) {
                        if (!friendsIDList.contains(id2) && !friendsOfFriendsIds.contains(id2) && !id2.equals(userid))
                            friendsOfFriendsIds.add(id2);
                    }
            } catch (AccesClosedException e) {
            } catch (PrivateProfileException e) {
            } catch (TooManyRequestsException e) {
                throw e;
            }
        }

        int curCount = 0;
        int counter = 0;
        ArrayList<Pair> resultList = new ArrayList<Pair>();

        for (Integer ffid : friendsOfFriendsIds) {
            Thread.sleep(timeOut);
            try {
                if (counter >= requestsCount) break;
                curCount = Worker.getMutualFriendsList(userid + "", ffid + "", userToken).size();
                resultList.add(new Pair(ffid + "", curCount));
                counter++;
            } catch (AccesClosedException e) {

            } catch (PrivateProfileException e) {

            } catch (TooManyRequestsException e) {
                throw e;
            }
        }

        System.out.println("************************************************************");
        System.out.println("Five people with the most count of common friends:");
        resultList.sort(Comparator.<Pair>reverseOrder());
        for (int i = 0; i < 5; i++) {
            System.out.println(Worker.getUserFullName(resultList.get(i).name, Worker.serviceToken) +
                    ": " + resultList.get(i).value);
        }
        System.out.println(" ");
        System.out.println("That's all!");
    }

}
