<?php

namespace App\Http\Controllers\API;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Models\Art;
use App\Models\User;
use GuzzleHttp\Psr7\Response;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class ArtController extends Controller
{
    public function index(){
        $art = Art::with('user:id,name')->get();
        return response() -> json($art);
    }

    public function store(Request $request){

        $owner = User::findOrFail($request->user_id);
        $pixels = $owner->pixels;
        if ($pixels < $request->cost){
            return response("You do not have enough pixels",401);
        }
        $art = new Art;
        $art->size = $request->size;
        $art->image = $request->image;
        $art -> poi_id = $request->poi_id;
        $art->user_id = $request->user_id;
        $art->save();
        $owner->pixels -= $request->cost;
        $owner->save();

        return response() -> json($art);
    }

    public function update(Request $request, int $id){
        $art = Art::findOrFail($id);
        $art->size = $request->size;
        $art->image = $request->image;
        $art -> poi_id = $request->poi_id;
        $art->user_id = $request->user_id;
        $art->reported = $request->reported;
        $art->update();
        return response() -> json($art);
    }

    public function destroy(int $id){
        $art = Art::findOrFail($id);
        $art->delete();
        return response()->json($art);
    }

    public function reported(){
        $art = Art::with('user:id,name')->where('reported',1)->get();
        if($art){
        return response() -> json($art);
        }
        else{
            return response("Nie ma żadnych zgłoszonych obrazków",401);
        }
    }

    public function reaction(Request $request, int $id){
        $user =  Auth::id();
        $criteria = [
            'user_id' => $user, // Replace with the actual user ID
            'art_id' => $id, // Replace with the actual art ID
        ];

        // Define the values to update or insert
        $values = [
            'liked' => $request->liked, // Replace with the actual value
        ];

        // Use updateOrInsert to update the existing row or insert a new one
        DB::table('likes')->updateOrInsert($criteria, $values);

        return response("Readted succesfuly",200);
    }

    public function updateUserScore(){
        $userId = Auth::id();
        $user = User::findOrFail($userId);
        $likes =  DB::table('likes')->where('user_id',"=",$userId)->where('liked','=',1)->get()->count();
        $dislikes =  DB::table('likes')->where('art_id',"=",$userId)->where('liked','=',0)->get()->count();
        $score = $likes - $dislikes;
        $user['score'] = $score;
        $user->update();
    }

    public function score(int $id){
        $likes =  DB::table('likes')->where('art_id',"=",$id)->where('liked','=',1)->get()->count();
        $dislikes =  DB::table('likes')->where('art_id',"=",$id)->where('liked','=',0)->get()->count();
        $score = $likes - $dislikes;
        $this->updateUserScore();

        return response()->json($score);
    }


}
