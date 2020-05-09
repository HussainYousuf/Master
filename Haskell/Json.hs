import Data.List

data JValue = JString String
            | JNumber Double
            | JBool Bool
            | JArray [JValue]
            | JObject [(String,JValue)]
            | JNull
            deriving (Show, Eq, Ord)

renderJValue (JObject object) = renderJValue' 0 (JObject object)
renderJValue (JArray array) = renderJValue' 0 (JArray array)
            
renderJValue' i (JString string) = show string           
renderJValue' i (JNumber number) = show number           
renderJValue' i (JBool True) = "true"
renderJValue' i (JBool False) = "false"
renderJValue' i (JNull) = "null"

renderJValue' i (JObject object) = "\n" ++ take (i) (repeat '\t') ++ "{" ++ render object ++ "\n" ++ take (i) (repeat '\t') ++ "}"
    where
        render object = intercalate ", " (map pair object)  
        pair (k,v) = "\n" ++ take (i+1) (repeat '\t') ++ show k ++ ": " ++ renderJValue' (i+1) v    
        
renderJValue' i (JArray array) = "\n" ++ take (i) (repeat '\t') ++ "[" ++ render array ++ "\n" ++ take (i) (repeat '\t') ++ "]"
    where
        render array = intercalate ", " (map (renderJValue' (i+1)) array)



type JSONError = String

class JSON a where    
    toJValue :: a -> JValue    
    fromJValue :: JValue -> Either JSONError a

instance JSON JValue where    
    toJValue = id    
    fromJValue = Right

    
instance JSON Bool where    
    toJValue = JBool    
    fromJValue (JBool b) = Right b    
    fromJValue _ = Left "not a JSON boolean"









        

{-           
getString (JString string) = Just string
getString _ = Nothing

getInt (JNumber number) = Just (truncate number)
getInt _ = Nothing

getDouble (JNumber number) = Just number
getDouble _ = Nothing

getBool (JBool bool) = Just bool
getBool _ = Nothing

isNull val = val == JNull  

-}
            
             
