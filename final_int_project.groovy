// ---------------------------
//
// HOMEWORK
//
// Use Groovy to write a code under "YOUR CODE GOES BELOW THIS LINE" comment.
// Make sure the code is working in some of the web Groovy consoles, e.g. https://groovyconsole.appspot.com
// Do not over-engineer the solution.
//
// Assume you got some data from a customer and your task is to design a routine that will calculate the average Product price per Group.
//
// The Price of each Product is calculated as:
// Cost * (1 + Margin)
//
// Assume there can be a large number of products.
//
// Plus points:
// - use Groovy closures (wherever it makes sense)
// - make the category look-up performance effective
 
def products = [
  ["A", "G1", 20.1],
  ["B", "G2", 98.4],
  ["C", "G1", 49.7],
  ["D", "G3", 35.8],
  ["E", "G3", 105.5],
  ["F", "G1", 55.2],
  ["G", "G1", 12.7],
  ["H", "G3", 88.6],
  ["I", "G1", 5.2],
  ["J", "G2", 72.4]
]

// contains information about Category classification based on product Cost
// [Category, Cost range from (inclusive), Cost range to (exclusive)]
// i.e. if a Product has Cost between 0 and 25, it belongs to category C1
// ranges are mutually exclusive and the last range has a null as upper limit.
def category = [
  ["C3", 50, 75],
  ["C4", 75, 100],
  ["C2", 25, 50],
  ["C5", 100, null],
  ["C1", 0, 25]
]


// contains information about margins for each product Category
// [Category, Margin (either percentage or absolute value)]
def margins = [
  "C1": "20%",
  "C2": "30%",
  "C3": "0.4",
  "C4": "50%",
  "C5": "0.6"
]

// ---------------------------
//
// YOUR CODE GOES BELOW THIS LINE


//Convert percent string to decimal

category=category.sort{it[2]} //Sorting for an efficient search
category.add(category.remove(0)) //Moving the 'null' to the back

def percent_str_to_decimal = {
  percent_str -> percent_str.replace("%", "").toDouble() / 100
}

//Convert margin strings to decimals if necessary
def normalize_margin_value = {
  margin_value -> margin_value[-1] == "%" ? percent_str_to_decimal(margin_value) : margin_value.toDouble()
}

//Calculate price
def get_price = {
  cost,
  margin -> cost * (1 + margin)
}

//To avoid searching the category array at every iteration later, adding margings values to category array
category.each{current_category->
  def category_name=current_category[0]
  current_category << normalize_margin_value(margins[category_name])
}

def group_totals = [:] //Price sum per group to avoid a second iteration
def group_counts = [:] //Number of entries per group counter to avoid a second iteration

products.each{current_product ->
  def current_group=current_product[1]
  def current_product_cost=current_product[2]
  def current_margin = category.find {it[2]==null || current_product_cost<it[2]}?.get(3) //Since my list is sorted, I only need to check the 'right' value
  def current_price=get_price(current_product_cost, current_margin)

  if (!group_totals[current_group])
    {      
    group_totals[current_group] = 0
    group_counts[current_group] = 0   
    }

  group_totals[current_group] += current_price
  group_counts[current_group]++
}


def averages = group_totals.collectEntries { group, total ->   [(group): (total / group_counts[group]).round(1)] }

//
// Assign the 'result' variable so the assertion at the end validates
//
// ---------------------------

result = averages 

 assert result == [
  "G1": 37.5,
  "G2": 124.5,
  "G3": 116.1
]: "It doesn't work"

println "It works!"