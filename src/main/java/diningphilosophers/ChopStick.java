package diningphilosophers;

// ESTADIEU JEAN 21/03/2025
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChopStick {
    
    private static int stickCount = 0;
    private boolean iAmFree = true;
    private final int myNumber;

    //Ajout du verou explicite
    //Permet de controler l'accèes concurrent à une ressource partagées entre plusieurs threads 
    private final static Lock lock = new ReentrantLock();
    public ChopStick() {
        myNumber = ++stickCount;
    }

    public boolean tryTake(int delay) throws InterruptedException {
        if (!iAmFree) {
            //TryLock (tente d'acquérir mais ne bloque pas)
            boolean locked = lock.tryLock(300, java.util.concurrent.TimeUnit.MILLISECONDS);
            if (!locked) // Toujours pas libre, on abandonne
            {
                return false; // Echec
            }
        }
        else{
            //Verouille
            //Un seul thread à la fois pourra entrer dans la section critique, les autres devrons attendre leur tour 
            lock.lock();
        }
        iAmFree = false;
        // Pas utile de faire notifyAll ici, personne n'attend qu'elle soit occupée
        return true; // Succès
    }

    public void release() {
        try {
            iAmFree = true;
            System.out.println("Stick " + myNumber + " Released");
        }
        finally {
            // Ouvrir le verrou 
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Stick#" + myNumber;
    }
}
