import Data.Char
import Data.List

take' n list = reverse $ drop' (length list - n) $ reverse list
    
drop' n list
  | n <= 0 = list
  | null list = []
  | True = drop' (n-1) xs    
  where x:xs = list
    
length' list
  | null list = 0
  | True = 1 + length' xs
  where x:xs = list
    
mean list = sum list / (fromIntegral $ length list :: Float)

isPalindrome list
  | length list < 2 = True 
  | x == head (reverse xs) = isPalindrome ( init xs ) 
  | True = False
  where x:xs = list
    
join sep list = take (length string - length sep) string
  where string = joinHelper sep list
    
joinHelper sep list
  | null list = []
  | True = x ++ sep ++ joinHelper sep xs
  where x:xs = list





-------------------------------------


quickSort [] = []
quickSort (x:xs) = quickSort smaller ++ [x] ++ larger
  where
    smaller = [a | a <- xs, a <= x]
    larger  = [b | b <- xs, b > x]

halve xs = splitAt (length xs `div` 2) xs

third (_:_:c:_) = c
third _ = error "less than 3 args in list"

luhnDouble x 
  | 2*x > 9 = 2*x - 9           
  | otherwise = 2*x

luhn n
  | sum (altMap id luhnDouble (int2xs n)) `mod` 10 == 0  = True 
  | otherwise = False

positions n xs = [i | (x,i) <- zip xs [0..], x == n]

count n xs = length (positions n xs)  

char2int c 
  | isLower c = ord c - ord 'a'
  | isUpper c = ord c - ord 'A' + 26
  | otherwise = ord c

int2char i 
  | isLower $ chr (i + ord 'a') = chr (i + ord 'a')
  | isUpper $ chr (i - 26 + ord 'A') = chr (i - 26 + ord 'A')
  | otherwise = chr i

shift n c 
  | isAlpha c = int2char ((char2int c + n) `mod` 52)
  | otherwise = c 

encode n xs = map (shift n) xs 

table' = [8.1, 1.5, 2.8, 4.2, 12.7, 2.2, 2.0, 6.1, 7.0,0.2, 0.8, 4.0, 2.4, 6.7, 7.5, 1.9, 0.1, 6.0,6.3, 9.0, 2.8, 1.0, 2.4, 0.2, 2.0, 0.1]

table = table' ++ table'

percent n m = (fromIntegral n / fromIntegral m) * 100

alphas xs = length [x | x <- xs, isAlpha x]

freqs xs = [percent (count x xs) n | x <- (['a'..'z'] ++ ['A'..'Z'])] 
  where 
    n = alphas xs
chisqr os es = sum [((o-e)^2)/e | (o,e) <- zip os es]

rotate n xs = drop n xs ++ take n xs

crack xs = encode (-factor) xs
  where
    factor = head (positions (minimum chitab) chitab)
    chitab = [chisqr (rotate x table') table | x <- [0..25]]
    table' = freqs xs

factors n = [x | x <- [1..n], n `mod` x == 0]

isPerfect n | (sum $ init $ factors n) == n = True | otherwise = False

insert' x [] = [x]
insert' y (x:xs) 
  | y <= x = y:x:xs
  | otherwise = x : insert y xs

iSort [] = []
iSort (x:xs) = insert x (iSort xs)

fib 0 = 0
fib 1 = 1
fib n = fib (n-1) + fib (n-2)

euclid x y 
  | x == y = x
  | x > y = euclid (x-y) y
  | x < y = euclid x (y-x)

and' (x:xs) 
  | null xs = x
  | otherwise = x && and' xs

merge xs [] = xs
merge [] ys = ys
merge (x:xs) (y:ys) 
  | x <= y = x : merge xs (y:ys)
  | otherwise = y : merge (x:xs) ys

mSort [] = []
mSort (x:xs) 
  | null xs = [x]
  | otherwise = merge (mSort lower) (mSort upper)
  where 
    halves = halve (x:xs)
    lower = fst halves
    upper = snd halves

int2bin n = reverse $ int2bin' n 

int2bin' 0 = []
int2bin' n = n `mod` 2 : int2bin' (n `div` 2)

make8 xs = reverse $ take 8 (reverse xs ++ repeat 0)

maken n xs = reverse $ take n (reverse xs ++ repeat 0)

bin2int xs = sum [x*2^i | (x,i) <- zip (reverse xs) [0..]]

encode' xs = concat $ map (make8 . int2bin . ord) xs

chop8 [] = []
chop8 xs = take 8 xs : (chop8 (drop 8 xs))

decode xs = map (chr . bin2int) xs

rmdups [] = []
rmdups (x:xs) = x : filter (/=x) (rmdups xs)

count' x xs = length $ filter (==x) xs

result xs = sort [(count x xs,x) | x <- (rmdups xs)]

int2xs n = reverse $ int2xs' n

int2xs' 0 = []
int2xs' n = n `mod` 10 : int2xs' (n `div` 10)

altMap f1 f2 xs = [ if (even i) then (f1 x) else (f2 x) | (x,i) <- zip xs [0..]]

xs2int xs = sum [x*10^i | (x,i) <- zip (reverse xs) [0..]]

data Nat = Zero | Succ Nat deriving Show

nat2int Zero = 0;
nat2int (Succ n) = 1 + nat2int n

int2nat 0 = Zero
int2nat n = Succ (int2nat (n-1))

data Tree a = Empty | Node a (Tree a) (Tree a) deriving Show

find' k' xs = head [v | (k,v) <- xs, k == k'] 

occurs x Empty = False
occurs x (Node a left right) = a == x || occurs x left ||  occurs x right 

inorder Empty = []
inorder (Node a left right) = inorder left ++ [a] ++ inorder right

safeTail [] = []
safeTail xs = tail xs

treeInsert xs = treeInsert'' xs Empty

treeInsert'' [] tree = tree
treeInsert'' (x:xs) tree  = treeInsert'' xs (treeInsert' x tree)

treeInsert' a Empty = Node a Empty Empty 
treeInsert' a (Node b left right)
  | a <= b = Node b (treeInsert' a left) right
  | otherwise = Node b left (treeInsert' a right) 

numOfLeaves Empty = 0
numOfLeaves (Node _ left right) = 1 + numOfLeaves left + numOfLeaves right

isBalanced Empty = True
isBalanced (Node _ left right) = if abs (numOfLeaves left - numOfLeaves right) > 1 then False else True

bools n = [map (\x -> if x == 0 then False else True) (maken n (int2bin x)) | x <- [0..2^n-1]] 

data Prop = Var Char
          | Not Prop
          | And Prop Prop
          | Imply Prop Prop

p1 = And (Var 'A') (Not (Var 'A'))
p2 = Imply (And (Var 'A') (Var 'B')) (Var 'A')
p3 = Imply (Var 'A') (And (Var 'A') (Var 'B'))
p4 = Imply (And (Var 'A') (Imply (Var 'A') (Var 'B'))) (Var 'B')

eval s (Var c) = find' c s
eval s (Not p) = not (eval s p)
eval s (And p1 p2) = (eval s p1) && (eval s p2)
eval s (Imply p1 p2) = (eval s p1) <= (eval s p2)

params (Var c) = [c]
params (Not p) = params p
params (And p q) = params p ++ params q
params (Imply p q) = params p ++ params q

truthTable params = map (zip params') bools' 
  where 
    bools' = bools (length params')
    params' = rmdups params

isTaut p = and [eval x p | x <- truthTable $ params p]

--------------------

data Op = Add | Sub | Mul | Div 

instance Show Op where 
  show Add = "+"
  show Sub = "-"
  show Mul = "*"
  show Div = "/"

valid Add _ _   = True
valid Sub n1 n2 = n1 > n2 
valid Mul _ _   = True
valid Div n1 n2 = n1 `mod` n2 == 0

apply Add n1 n2 = n1 + n2
apply Sub n1 n2 = n1 - n2
apply Mul n1 n2 = n1 * n2
apply Div n1 n2 = n1 `div` n2

data Expr = Val Int | App Op Expr Expr

instance Show Expr where
  show (Val n) = show n
  show (App o e1 e2) = show' e1 ++ show o ++ show' e2
    where 
      show' (Val n) = show (Val n)
      show' e       = "(" ++ show e ++ ")"

values (Val n) = [n]
values (App _ e1 e2) = values e1 ++ values e2

eval' (Val n) = [n | n > 0]
eval' (App o e1 e2) = [apply o x y | x <- eval' e1, y <- eval' e2, valid o x y]

subs [] = [[]]
subs (x:xs) = yss ++ map (x:) yss
  where yss = subs xs

subset xs = map (\ys -> [x | (b,x) <- zip (reverse ys) xs, b]) (bools $ length xs)

swap' prev (x:xs) 
  | xs == []  = [] 
  | otherwise = (prev ++ (head xs : x : tail xs)) : [] ++ swap' (prev ++ [head xs]) (x : tail xs)

interleave y [] = [[y]]
interleave y xs = [y:xs] ++ swap' [] (y:xs)

-- perms [] = [[]]
-- perms (x:xs) = concat $ map (interleave x) (perms xs)

rotations xs = rotations' (length xs) xs

rotations' 0 _  = []
rotations' n xs = rotate n xs : rotations' (n-1) xs

perms [] = [[]]
perms (x:xs) = concat $ map rotations $ map (x:) $ perms xs

choices = concat . map perms . subset

solution e ns n = elem (values e) (choices ns) && eval' e == [n]

reverse' [] = []
reverse' (x:xs) = reverse' xs ++ [x]

split xs = reverse $ split' (length xs) xs

split' 1 xs = []
split' n xs = splitAt (n-1) xs : split' (n-1) xs

