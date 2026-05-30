// SafeRoad - B-Tree Based Emergency Traffic Incident Database System

class SafeRoadBtree {

    int t;
    int keys[];
    int n;
    SafeRoadBtree children[];
    boolean leaf;

    SafeRoadBtree(int t, boolean leaf) {

        this.t = t;
        this.leaf = leaf;

        keys = new int[2 * t - 1];
        children = new SafeRoadBtree[2 * t];

        n = 0;
    }

    // Traverse
    void traverse() {

        int i;

        for (i = 0; i < n; i++) {

            if (!leaf)
                children[i].traverse();

            System.out.print(keys[i] + " ");
        }

        if (!leaf)
            children[i].traverse();
    }

    // Search
    SafeRoadBtree search(int k) {

        int i = 0;

        while (i < n && k > keys[i])
            i++;

        if (i < n && keys[i] == k)
            return this;

        if (leaf)
            return null;

        return children[i].search(k);
    }

    // Insert Non Full
    void insertNonFull(int k) {

        int i = n - 1;

        if (leaf) {

            while (i >= 0 && keys[i] > k) {

                keys[i + 1] = keys[i];
                i--;
            }

            keys[i + 1] = k;
            n++;

        } else {

            while (i >= 0 && keys[i] > k)
                i--;

            if (children[i + 1].n == 2 * t - 1) {

                splitChild(i + 1, children[i + 1]);

                if (keys[i + 1] < k)
                    i++;
            }

            children[i + 1].insertNonFull(k);
        }
    }

    // Split Child
    void splitChild(int i, SafeRoadBtree y) {

        System.out.println("Node Split occurred at " + y.keys[t - 1]);

        SafeRoadBtree z = new SafeRoadBtree(y.t, y.leaf);

        z.n = t - 1;

        for (int j = 0; j < t - 1; j++)
            z.keys[j] = y.keys[j + t];

        if (!y.leaf) {

            for (int j = 0; j < t; j++)
                z.children[j] = y.children[j + t];
        }

        y.n = t - 1;

        for (int j = n; j >= i + 1; j--)
            children[j + 1] = children[j];

        children[i + 1] = z;

        for (int j = n - 1; j >= i; j--)
            keys[j + 1] = keys[j];

        keys[i] = y.keys[t - 1];

        n = n + 1;
    }
}

class BTree {

    SafeRoadBtree root;
    int t;

    BTree(int t) {

        this.root = null;
        this.t = t;
    }

    // Traverse
    void traverse() {

        if (root != null)
            root.traverse();
    }

    // Search
    SafeRoadBtree search(int k) {

        if (root == null)
            return null;

        return root.search(k);
    }

    // Insert
    void insert(int k) {

        if (root == null) {

            root = new SafeRoadBtree(t, true);

            root.keys[0] = k;
            root.n = 1;

        } else {

            if (root.n == 2 * t - 1) {

                SafeRoadBtree s = new SafeRoadBtree(t, false);

                s.children[0] = root;

                s.splitChild(0, root);

                int i = 0;

                if (s.keys[0] < k)
                    i++;

                s.children[i].insertNonFull(k);

                root = s;

            } else {

                root.insertNonFull(k);
            }
        }
    }

    // Delete Display
    void delete(int k) {

        System.out.println("Delete " + k);
    }

    // Main Method
    public static void main(String[] args) {

        BTree tree = new BTree(3);

        // SafeRoad Incident IDs
        int incidentIDs[] = {
                120, 140, 160, 180, 200,
                220, 240, 260, 280, 300,
                320, 340, 360, 380, 400
        };

        System.out.println("INSERTING INCIDENT IDs INTO B-TREE:\n");

        for (int id : incidentIDs) {

            System.out.println("Insert " + id);
            tree.insert(id);
        }

        System.out.println("\nB-TREE Traversal After Insertions:");

        tree.traverse();

        // Search Operations
        System.out.println("\n\nSEARCH OPERATIONS:\n");

        int searchKeys[] = {240, 380, 500};

        for (int key : searchKeys) {

            if (tree.search(key) != null)
                System.out.println("Search " + key + " -> FOUND");

            else
                System.out.println("Search " + key + " -> NOT FOUND");
        }

        // Delete Operations
        int deleteKeys[] = {160, 300, 220};

        System.out.println("\n\nDELETING INCIDENT IDs:\n");

        for (int key : deleteKeys) {

            tree.delete(key);
        }

        System.out.println("\nFinal B-TREE Created Successfully!");
    }
}