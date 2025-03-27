package dsa_Metro;




import java.util.*;
import java.io.*;

	
	public class Graph_M 
	{
		public class Vertex 
		{
			HashMap<String, Integer> nbrs = new HashMap<>();
		}

		static HashMap<String, Vertex> vtces;

		public Graph_M() 
		{
			vtces = new HashMap<>();
		}	

		public int numVetex() 
		{
			return this.vtces.size();
		}

		public boolean containsVertex(String vname) 
		{
			return this.vtces.containsKey(vname);
		}

		public void addVertex(String vname) 
		{
			Vertex vtx = new Vertex();
			vtces.put(vname, vtx);
		}

		public void removeVertex(String vname) 
		{
			Vertex vtx = vtces.get(vname);
			ArrayList<String> keys = new ArrayList<>(vtx.nbrs.keySet());

			for (String key : keys) 
			{
				Vertex nbrVtx = vtces.get(key);
				nbrVtx.nbrs.remove(vname);
			}

			vtces.remove(vname);
		}

		public int numEdges() 
		{
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());
			int count = 0;

			for (String key : keys) 
			{
				Vertex vtx = vtces.get(key);
				count = count + vtx.nbrs.size();
			}

			return count / 2;
		}

		public boolean containsEdge(String vname1, String vname2) 
		{
			Vertex vtx1 = vtces.get(vname1);
			Vertex vtx2 = vtces.get(vname2);
			
			if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
				return false;
			}

			return true;
		}

		public void addEdge(String vname1, String vname2, int value) 
		{
			Vertex vtx1 = vtces.get(vname1); 
			Vertex vtx2 = vtces.get(vname2); 

			if (vtx1 == null || vtx2 == null || vtx1.nbrs.containsKey(vname2)) {
				return;
			}

			vtx1.nbrs.put(vname2, value);
			vtx2.nbrs.put(vname1, value);
		}

		public void removeEdge(String vname1, String vname2) 
		{
			Vertex vtx1 = vtces.get(vname1);
			Vertex vtx2 = vtces.get(vname2);
			
			//check if the vertices given or the edge between these vertices exist or not
			if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
				return;
			}

			vtx1.nbrs.remove(vname2);
			vtx2.nbrs.remove(vname1);
		}

		public void display_Map() 
		{
			System.out.println("\t NAMMA BANGALORE Metro Map");
			System.out.println("\t------------------");
			System.out.println("----------------------------------------------------\n");
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());

			for (String key : keys) 
			{
				String str = key + " =>\n";
				Vertex vtx = vtces.get(key);
				ArrayList<String> vtxnbrs = new ArrayList<>(vtx.nbrs.keySet());
				
				for (String nbr : vtxnbrs)
				{
					str = str + "\t" + nbr + "\t";
                    			if (nbr.length()<16)
                    			str = str + "\t";
                    			if (nbr.length()<8)
                    			str = str + "\t";
                    			str = str + vtx.nbrs.get(nbr) + "\n";
				}
				System.out.println(str);
			}
			System.out.println("\t------------------");
			System.out.println("---------------------------------------------------\n");

		}
		
		public void display_Stations() 
		{
			System.out.println("\n***********************************************************************\n");
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());
			int i=1;
			for(String key : keys) 
			{
				System.out.println(i + ". " + key);
				i++;
			}
			System.out.println("\n***********************************************************************\n");
		}
			
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public boolean hasPath(String vname1, String vname2, HashMap<String, Boolean> processed) 
		{
			// DIR EDGE
			if (containsEdge(vname1, vname2)) {
				return true;
			}

			//MARK AS DONE
			processed.put(vname1, true);

			Vertex vtx = vtces.get(vname1);
			ArrayList<String> nbrs = new ArrayList<>(vtx.nbrs.keySet());

			//TRAVERSE THE NBRS OF THE VERTEX
			for (String nbr : nbrs) 
			{

				if (!processed.containsKey(nbr))
					if (hasPath(nbr, vname2, processed))
						return true;
			}

			return false;
		}
		
		
		private class DijkstraPair implements Comparable<DijkstraPair> 
		{
			String vname;
			String psf;
			int cost;

			/*
			The compareTo method is defined in Java.lang.Comparable.
			Here, we override the method because the conventional compareTo method
			is used to compare strings,integers and other primitive data types. But
			here in this case, we intend to compare two objects of DijkstraPair class.
			*/ 

			/*
			Removing the overriden method gives us this errror:
			The type Graph_M.DijkstraPair must implement the inherited abstract method Comparable<Graph_M.DijkstraPair>.compareTo(Graph_M.DijkstraPair)

			This is because DijkstraPair is not an abstract class and implements Comparable interface which has an abstract 
			method compareTo. In order to make our class concrete(a class which provides implementation for all its methods)
			we have to override the method compareTo
			 */
			@Override
			public int compareTo(DijkstraPair o) 
			{
				return o.cost - this.cost;
			}
		}
		
		public int dijkstra(String src, String des, boolean nan) 
		{
			int val = 0;
			ArrayList<String> ans = new ArrayList<>();
			HashMap<String, DijkstraPair> map = new HashMap<>();

			Heap<DijkstraPair> heap = new Heap<>();

			for (String key : vtces.keySet()) 
			{
				DijkstraPair np = new DijkstraPair();
				np.vname = key;
				//np.psf = "";
				np.cost = Integer.MAX_VALUE;

				if (key.equals(src)) 
				{
					np.cost = 0;
					np.psf = key;
				}

				heap.add(np);
				map.put(key, np);
			}

			//keep removing the pairs while heap is not empty
			while (!heap.isEmpty()) 
			{
				DijkstraPair rp = heap.remove();
				
				if(rp.vname.equals(des))
				{
					val = rp.cost;
					break;
				}
				
				map.remove(rp.vname);

				ans.add(rp.vname);
				
				Vertex v = vtces.get(rp.vname);
				for (String nbr : v.nbrs.keySet()) 
				{
					if (map.containsKey(nbr)) 
					{
						int oc = map.get(nbr).cost;
						Vertex k = vtces.get(rp.vname);
						int nc;
						if(nan)
							nc = rp.cost + 120 + 40*k.nbrs.get(nbr);
						else
							nc = rp.cost + k.nbrs.get(nbr);

						if (nc < oc) 
						{
							DijkstraPair gp = map.get(nbr);
							gp.psf = rp.psf + nbr;
							gp.cost = nc;

							heap.updatePriority(gp);
						}
					}
				}
			}
			return val;
		}
		
		private class Pair 
		{
			String vname;
			String psf;
			int min_dis;
			int min_time;
		}
		
		public String Get_Minimum_Distance(String src, String dst) 
		{
			int min = Integer.MAX_VALUE;
			//int time = 0;
			String ans = "";
			HashMap<String, Boolean> processed = new HashMap<>();
			LinkedList<Pair> stack = new LinkedList<>();

			// create a new pair
			Pair sp = new Pair();
			sp.vname = src;
			sp.psf = src + "  ";
			sp.min_dis = 0;
			sp.min_time = 0;
			
			// put the new pair in stack
			stack.addFirst(sp);

			// while stack is not empty keep on doing the work
			while (!stack.isEmpty()) 
			{
				// remove a pair from stack
				Pair rp = stack.removeFirst();

				if (processed.containsKey(rp.vname)) 
				{
					continue;
				}

				// processed put
				processed.put(rp.vname, true);
				
				//if there exists a direct edge b/w removed pair and destination vertex
				if (rp.vname.equals(dst)) 
				{
					int temp = rp.min_dis;
					if(temp<min) {
						ans = rp.psf;
						min = temp;
					}
					continue;
				}

				Vertex rpvtx = vtces.get(rp.vname);
				ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

				for(String nbr : nbrs) 
				{
					// process only unprocessed nbrs
					if (!processed.containsKey(nbr)) {

						// make a new pair of nbr and put in queue
						Pair np = new Pair();
						np.vname = nbr;
						np.psf = rp.psf + nbr + "  ";
						np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr); 
						//np.min_time = rp.min_time + 120 + 40*rpvtx.nbrs.get(nbr); 
						stack.addFirst(np);
					}
				}
			}
			ans = ans + Integer.toString(min);
			return ans;
		}
		
		
		public String Get_Minimum_Time(String src, String dst) 
		{
			int min = Integer.MAX_VALUE;
			String ans = "";
			HashMap<String, Boolean> processed = new HashMap<>();
			LinkedList<Pair> stack = new LinkedList<>();

			// create a new pair
			Pair sp = new Pair();
			sp.vname = src;
			sp.psf = src + "  ";
			sp.min_dis = 0;
			sp.min_time = 0;
			
			// put the new pair in queue
			stack.addFirst(sp);

			// while queue is not empty keep on doing the work
			while (!stack.isEmpty()) {

				// remove a pair from queue
				Pair rp = stack.removeFirst();

				if (processed.containsKey(rp.vname)) 
				{
					continue;
				}

				// processed put
				processed.put(rp.vname, true);

				//if there exists a direct edge b/w removed pair and destination vertex
				if (rp.vname.equals(dst)) 
				{
					int temp = rp.min_time;
					if(temp<min) {
						ans = rp.psf;
						min = temp;
					}
					continue;
				}

				Vertex rpvtx = vtces.get(rp.vname);
				ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

				for (String nbr : nbrs) 
				{
					// process only unprocessed nbrs
					if (!processed.containsKey(nbr)) {

						// make a new pair of nbr and put in queue
						Pair np = new Pair();
						np.vname = nbr;
						np.psf = rp.psf + nbr + "  ";
						//np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr);
						np.min_time = rp.min_time + 120 + 40*rpvtx.nbrs.get(nbr); 
						stack.addFirst(np);
					}
				}
			}
			Double minutes = Math.ceil((double)min / 60);
			ans = ans + Double.toString(minutes);
			return ans;
		}
		
		public ArrayList<String> get_Interchanges(String str)
		{
			ArrayList<String> arr = new ArrayList<>();
			String res[] = str.split("  ");
			arr.add(res[0]);
			int count = 0;
			for(int i=1;i<res.length-1;i++)
			{
				int index = res[i].indexOf('~');
				String s = res[i].substring(index+1);
				
				if(s.length()==2)
				{
					String prev = res[i-1].substring(res[i-1].indexOf('~')+1);
					String next = res[i+1].substring(res[i+1].indexOf('~')+1);
					
					if(prev.equals(next)) 
					{
						arr.add(res[i]);
					}
					else
					{
						arr.add(res[i]+" ==> "+res[i+1]);
						i++;
						count++;
					}
				}
				else
				{
					arr.add(res[i]);
				}
			}
			arr.add(Integer.toString(count));
			arr.add(res[res.length-1]);
			return arr;
		}
		
public static void Create_Metro_Map(Graph_M g) {
	        // Add stations and edges for the Purple line
	        g.addVertex("Whitefield~P");
	        g.addVertex("Kadugodi Tree Park~P");
	        g.addVertex("Pattanduru Agrahara~P");
	        g.addVertex("Sri Sathya Sai Hospital~P");
	        g.addVertex("Nallurhalli~P");
	        g.addVertex("Kundalahalli~P");
	        g.addVertex("Seetharamapalya~P");
	        g.addVertex("Hoodi~P");
	        g.addVertex("Garudacharapalya~P");
	        g.addVertex("Singayyanapalya~P");
	        g.addVertex("Krishnarajapura (K.R.Pura)~P");
	        g.addVertex("Benniganahalli~P");
	        g.addVertex("Baiyappanahalli~P");
	        g.addVertex("Swami Vivekananda Road~P");
	        g.addVertex("Indiranagara~P");
	        g.addVertex("Halasuru~P");
	        g.addVertex("Trinity~P");
	        g.addVertex("MG Road~P");
	        g.addVertex("Cubbon Park~P");
	        g.addVertex("Dr. BR. Ambedkar Station (Vidhana Soudha)~P");
	        g.addVertex("Sir M. Visveshwaraya Station (Central College)~P");
	        g.addVertex("City Railway Station~P");
	        g.addVertex("Magadi Road~P");
	        g.addVertex("Sri Balagangadharanatha Swamiji Station (Hosahalli)~P");
	        g.addVertex("Vijayanagara~P");
	        g.addVertex("Attiguppe~P");
	        g.addVertex("Deepanjali Nagara~P");
	        g.addVertex("Mysuru Road~P");
	        g.addVertex("Pantharapalya (Nayandahalli)~P");
	        g.addVertex("Rajarajeshwari Nagar~P");
	        g.addVertex("Jnanabharathi~P");
	        g.addVertex("Pattanagere~P");
	        g.addVertex("Kengeri Bus Terminal~P");
	        g.addVertex("Kengeri~P");
	        g.addVertex("Challaghatta~P");
            
	        // Add stations and edges for the Green  line
	        g.addVertex("Nagasandra~G");
	        g.addVertex("Dasarahalli~G");
	        g.addVertex("Jalahalli~G");
	        g.addVertex("Peenya Industry~G");
	        g.addVertex("Peenya~G");
	        g.addVertex("Yeshwanthpur~G");
	        g.addVertex("Sandal Soap Factory~G");
	        g.addVertex("Mahalakshmi~G");
	        g.addVertex("Rajajinagara~G");
	        g.addVertex("Mahakavi Kuvempu Road~G");
	        g.addVertex("Srirampura~G");
	        g.addVertex("Mantri Square Sampige Road~G");
	        g.addVertex("Chikkapete~G");
	        g.addVertex("Krishna Rajendra Market~G");
	        g.addVertex("National College~G");
	        g.addVertex("Lalbagh~G");
	        g.addVertex("South End Circle~G");
	        g.addVertex("Jayanagara~G");
	        g.addVertex("Rashtreeya Vidyalaya Road~G");
	        g.addVertex("Banashankari~G");
	        g.addVertex("Jayaprakash Nagara~G");
	        g.addVertex("Yelachenahalli~G");
	        g.addVertex("Konanakunte Cross~G");
	        g.addVertex("Doddakallasandra~G");
	        g.addVertex("Vajarahalli~G");
	        g.addVertex("Thalaghattapura~G");
	        g.addVertex("Silk Institute~G");
	        
	        //Interchange Station (Junction Station)
	        g.addVertex("Nadaprabhu Kempegowda Station, Majestic~PG");

	        //Distance between Two Stations (Purple Line)
	        g.addEdge("Whitefield~P", "Kadugodi Tree Park~P", 1);
	        g.addEdge("Kadugodi Tree Park~P", "Pattanduru Agrahara~P", 1);
	        g.addEdge("Pattanduru Agrahara~P", "Sri Sathya Sai Hospital~P", 1);
	        g.addEdge("Sri Sathya Sai Hospital~P", "Nallurhalli~P", 1);
	        g.addEdge("Nallurhalli~P", "Kundalahalli~P", 1);
	        g.addEdge("Kundalahalli~P", "Seetharamapalya~P", 1);
	        g.addEdge("Seetharamapalya~P", "Hoodi~P", 1);
	        g.addEdge("Hoodi~P", "Garudacharapalya~P", 1);
	        g.addEdge("Garudacharapalya~P", "Singayyanapalya~P", 1);
	        g.addEdge("Singayyanapalya~P", "Krishnarajapura (K.R.Pura)~P", 2);
	        g.addEdge("Krishnarajapura (K.R.Pura)~P", "Benniganahalli~P", 1);
	        g.addEdge("Benniganahalli~P", "Baiyappanahalli~P", 2);
	        g.addEdge("Baiyappanahalli~P", "Swami Vivekananda Road~P", 1);
	        g.addEdge("Swami Vivekananda Road~P", "Indiranagara~P", 1);
	        g.addEdge("Indiranagara~P", "Halasuru~P", 1);
	        g.addEdge("Halasuru~P", "Trinity~P", 1);
	        g.addEdge("Trinity~P", "MG Road~P", 1);
	        g.addEdge("MG Road~P", "Cubbon Park~P", 1);
	        g.addEdge("Cubbon Park~P", "Dr. BR. Ambedkar Station (Vidhana Soudha)~P", 1);
	        g.addEdge("Dr. BR. Ambedkar Station (Vidhana Soudha)~P", "Sir M. Visveshwaraya Station (Central College)~P", 1);
	        g.addEdge("Sir M. Visveshwaraya Station (Central College)~P", "Nadaprabhu Kempegowda Station, Majestic~PG", 1);
	        g.addEdge("Nadaprabhu Kempegowda Station, Majestic~PG", "City Railway Station~P", 1);
	        g.addEdge("City Railway Station~P", "Magadi Road~P", 1);
	        g.addEdge("Magadi Road~P", "Sri Balagangadharanatha Swamiji Station (Hosahalli~P)", 1);
	        g.addEdge("Sri Balagangadharanatha Swamiji Station (Hosahalli)~P", "Vijayanagara~P", 1);
	        g.addEdge("Vijayanagara~P", "Attiguppe~P", 1);
	        g.addEdge("Attiguppe~P", "Deepanjali Nagara~P", 1);
	        g.addEdge("Deepanjali Nagara~P", "Mysuru Road~P", 1);
	        g.addEdge("Mysuru Road~P", "Pantharapalya (Nayandahalli)~P", 1);
	        g.addEdge("Pantharapalya (Nayandahalli)~P", "Rajarajeshwari Nagar~P", 1);
	        g.addEdge("Rajarajeshwari Nagar~P", "Jnanabharathi~P", 1);
	        g.addEdge("Jnanabharathi~P", "Pattanagere~P", 1);
	        g.addEdge("Pattanagere~P", "Kengeri Bus Terminal~P", 5);
	        g.addEdge("Kengeri Bus Terminal~P", "Kengeri~P", 2);
	        g.addEdge("Kengeri~P", "Challaghatta~P", 6);

	      
	        //Distance between Two Stations (Green Line)

	        g.addEdge("Nagasandra~G", "Dasarahalli~G", 1);
	        g.addEdge("Dasarahalli~G", "Jalahalli~G", 1);
	        g.addEdge("Jalahalli~G", "Peenya Industry~G", 1);
	        g.addEdge("Peenya Industry~G", "Peenya~G", 1);
	        g.addEdge("Peenya~G", "Yeshwanthpur~G", 2);
	        g.addEdge("Yeshwanthpur~G", "Sandal Soap Factory~G", 1);
	        g.addEdge("Sandal Soap Factory~G", "Mahalakshmi~G", 1);
	        g.addEdge("Mahalakshmi~G", "Rajajinagara~G", 2);
	        g.addEdge("Rajajinagara~G", "Mahakavi Kuvempu Road~G", 1);
	        g.addEdge("Mahakavi Kuvempu Road~G", "Srirampura~G", 1);
	        g.addEdge("Srirampura~G", "Mantri Square Sampige Road~G", 1);
	        g.addEdge("Mantri Square Sampige Road~G", "Nadaprabhu Kempegowda Station, Majestic~PG", 2);
	        g.addEdge("Nadaprabhu Kempegowda Station, Majestic~PG", "Chikkapete~G", 1);
	        g.addEdge("Chikkapete~G", "Krishna Rajendra Market~G", 1);
	        g.addEdge("Krishna Rajendra Market~G", "National College~G", 1);
	        g.addEdge("National College~G", "Lalbagh~G", 1);
	        g.addEdge("Lalbagh~G", "South End Circle~G", 1);
	        g.addEdge("South End Circle~G", "Jayanagara~G", 1);
	        g.addEdge("Jayanagara~G", "Rashtreeya Vidyalaya Road~G", 1);
	        g.addEdge("Rashtreeya Vidyalaya Road~G", "Banashankari~G", 2);
	        g.addEdge("Banashankari~G", "Jayaprakash Nagara~G", 1);
	        g.addEdge("Jayaprakash Nagara~G", "Yelachenahalli~G", 1);
	        g.addEdge("Yelachenahalli~G", "Konanakunte Cross~G", 1);
	        g.addEdge("Konanakunte Cross~G", "Doddakallasandra~G", 1);
	        g.addEdge("Doddakallasandra~G", "Vajarahalli~G", 1);
	        g.addEdge("Vajarahalli~G", "Thalaghattapura~G", 1);
	        g.addEdge("Thalaghattapura~G", "Silk Institute~G", 1);

	    }
				
		public static String[] printCodelist()
		{
			System.out.println("List of station along with their codes:\n");
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());
			int i=1,j=0,m=1;
			StringTokenizer stname;
			String temp="";
			String codes[] = new String[keys.size()];
			char c;
			for(String key : keys) 
			{
				stname = new StringTokenizer(key);
				codes[i-1] = "";
				j=0;
				while (stname.hasMoreTokens())
				{
				        temp = stname.nextToken();
				        c = temp.charAt(0);
				        while (c>47 && c<58)
				        {
				                codes[i-1]+= c;
				                j++;
				                c = temp.charAt(j);
				        }
				        if ((c<48 || c>57) && c<123)
				                codes[i-1]+= c;
				}
				if (codes[i-1].length() < 2)
					codes[i-1]+= Character.toUpperCase(temp.charAt(1));
				            
				System.out.print(i + ". " + key + "\t");
				if (key.length()<(22-m))
                    			System.out.print("\t");
				if (key.length()<(14-m))
                    			System.out.print("\t");
                    		if (key.length()<(6-m))
                    			System.out.print("\t");
                    		System.out.println(codes[i-1]);
				i++;
				if (i == (int)Math.pow(10,m))
				        m++;
			}
			return codes;
		}
		
		public static void main(String[] args) throws IOException
		{
			Graph_M g = new Graph_M();
			Create_Metro_Map(g);
			
			System.out.println("\n\t\t\t****WELCOME TO THE METRO APP*****");
			// System.out.println("\t\t\t\t~~LIST OF ACTIONS~~\n\n");
			// System.out.println("1. LIST ALL THE STATIONS IN THE MAP");
			// System.out.println("2. SHOW THE METRO MAP");
			// System.out.println("3. GET SHORTEST DISTANCE FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			// System.out.println("4. GET SHORTEST TIME TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			// System.out.println("5. GET SHORTEST PATH (DISTANCE WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			// System.out.println("6. GET SHORTEST PATH (TIME WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			// System.out.print("\nENTER YOUR CHOICE FROM THE ABOVE LIST : ");
			BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
			// int choice = Integer.parseInt(inp.readLine());
			//STARTING SWITCH CASE
			while(true)
			{
				System.out.println("\t\t\t\t~~LIST OF ACTIONS~~\n\n");
				System.out.println("1. LIST ALL THE STATIONS IN THE MAP");
				System.out.println("2. SHOW THE METRO MAP");
				System.out.println("3. GET SHORTEST DISTANCE FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("4. GET SHORTEST TIME TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("5. GET SHORTEST PATH (DISTANCE WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("6. GET SHORTEST PATH (TIME WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("7. EXIT THE MENU");
				System.out.print("\nENTER YOUR CHOICE FROM THE ABOVE LIST (1 to 7) : ");
				int choice = -1;
				try {
					choice = Integer.parseInt(inp.readLine());
				} catch(Exception e) {
					// default will handle
				}
				System.out.print("\n***********************************************************\n");
				if(choice == 7)
				{
					System.exit(0);
				}
				switch(choice)
				{
				case 1:
					g.display_Stations();
					break;
			
				case 2:
					g.display_Map();
					break;
				
				case 3:
					ArrayList<String> keys = new ArrayList<>(vtces.keySet());
					String codes[] = printCodelist();
					System.out.println("\n1. TO ENTER SERIAL NO. OF STATIONS\n2. TO ENTER CODE OF STATIONS\n3. TO ENTER NAME OF STATIONS\n");
					System.out.println("ENTER YOUR CHOICE:");
				        int ch = Integer.parseInt(inp.readLine());
					int j;
						
					String st1 = "", st2 = "";
					System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
					if (ch == 1)
					{
					    st1 = keys.get(Integer.parseInt(inp.readLine())-1);
					    st2 = keys.get(Integer.parseInt(inp.readLine())-1);
					}
					else if (ch == 2)
					{
					    String a,b;
					    a = (inp.readLine()).toUpperCase();
					    for (j=0;j<keys.size();j++)
					       if (a.equals(codes[j]))
					           break;
					    st1 = keys.get(j);
					    b = (inp.readLine()).toUpperCase();
					    for (j=0;j<keys.size();j++)
					       if (b.equals(codes[j]))
					           break;
					    st2 = keys.get(j);
					}
					else if (ch == 3)
					{
					    st1 = inp.readLine();
					    st2 = inp.readLine();
					}
					else
					{
					    System.out.println("Invalid choice");
					    System.exit(0);
					}
				
					HashMap<String, Boolean> processed = new HashMap<>();
					if(!g.containsVertex(st1) || !g.containsVertex(st2) || !g.hasPath(st1, st2, processed))
						System.out.println("THE INPUTS ARE INVALID");
					else
					System.out.println("SHORTEST DISTANCE FROM "+st1+" TO "+st2+" IS "+g.dijkstra(st1, st2, false)+"KM\n");
					break;
				
				case 4:
					String codes1[] = printCodelist();

					System.out.print("ENTER THE SOURCE STATION: ");
					String sat1 = inp.readLine();
					System.out.print("ENTER THE DESTINATION STATION: ");
					String sat2 = inp.readLine();
				
					HashMap<String, Boolean> processed1= new HashMap<>();				
					System.out.println("SHORTEST TIME FROM ("+sat1+") TO ("+sat2+") IS "+g.dijkstra(sat1, sat2, true)/60+" MINUTES\n\n");
					break;
				
				case 5:
					String codes2[] = printCodelist();

					System.out.println("ENTER THE SOURCE STATION: ");
					String s1 = inp.readLine();
					System.out.print("ENTER THE DESTINATION STATION: ");

					String s2 = inp.readLine();
				
					HashMap<String, Boolean> processed2 = new HashMap<>();
					if(!g.containsVertex(s1) || !g.containsVertex(s2) || !g.hasPath(s1, s2, processed2))
						System.out.println("THE INPUTS ARE INVALID");
					else 
					{
						ArrayList<String> str = g.get_Interchanges(g.Get_Minimum_Distance(s1, s2));
						int len = str.size();
						System.out.println("SOURCE STATION : " + s1);
						System.out.println("DESTINATION STATION : " + s2);
						System.out.println("DISTANCE : " + str.get(len-1));
						System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));
						//System.out.println(str);
						System.out.println("~~~~~~~~~~~~~");
						System.out.println("START  ==>  " + str.get(0));
						for(int i=1; i<len-3; i++)
						{
							System.out.println(str.get(i));
						}
						System.out.print(str.get(len-3) + "   ==>    END");
						System.out.println("\n~~~~~~~~~~~~~");
					}
					break;
				
				case 6:
					String codes3[] = printCodelist();

					System.out.print("ENTER THE SOURCE STATION: ");
					String ss1 = inp.readLine();
					System.out.print("ENTER THE DESTINATION STATION: ");
					String ss2 = inp.readLine();
				
					HashMap<String, Boolean> processed3 = new HashMap<>();
					if(!g.containsVertex(ss1) || !g.containsVertex(ss2) || !g.hasPath(ss1, ss2, processed3))
						System.out.println("THE INPUTS ARE INVALID");
					else
					{
						ArrayList<String> str = g.get_Interchanges(g.Get_Minimum_Time(ss1, ss2));
						int len = str.size();
						System.out.println("SOURCE STATION : " + ss1);
						System.out.println("DESTINATION STATION : " + ss2);
						System.out.println("TIME : " + str.get(len-1)+" MINUTES");
						System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));
						//System.out.println(str);
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						System.out.print("START  ==>  " + str.get(0) + " ==>  ");
						for(int i=1; i<len-3; i++)
						{
							System.out.println(str.get(i));
						}
						System.out.print(str.get(len-3) + "   ==>    END");
						System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					}
					break;	
               	         default:  //If switch expression does not match with any case, 
                	        	//default statements are executed by the program.
                            	//No break is needed in the default case
                    	        System.out.println("Please enter a valid option! ");
                        	    System.out.println("The options you can choose are from 1 to 6. ");
                            
				}
			}
			
		}	
	}
	
