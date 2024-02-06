<?php

namespace App\Http\Controllers\API;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class UserController extends Controller
{
    public function updatePixels(Request $request){
        $userId = Auth::id();
        $user = User::findOrFail($userId);
        $user->pixels = $request->pixels;
        $user->update();
        return response($user);
    }
    public function hasShaken(){
        $user = User::findOrFail(Auth::id());
        if ($user->last_execution_date != now()->toDateString()) {
            // Your API logic here

            // Update last execution date to the current date
            $user->update(['last_execution_date' => now()->toDateString()]);

            return response()->json(['success' => 'API function executed successfully.'],200);
        }

        return response()->json('Ta akcja została już dzisiaj wykonana', 429);
    }
    public function getUser(){
        $userId = Auth::id();
        $user = User::findOrFail($userId);
        $likes =  DB::table('likes')->where('user_id',"=",$userId)->where('liked','=',1)->get()->count();
        $dislikes =  DB::table('likes')->where('art_id',"=",$userId)->where('liked','=',0)->get()->count();
        $score = $likes - $dislikes;
        $user['score'] = $score;
        return response($user);
    }
    public function getUserWithId(int $id){
        $user = User::with('art')->find($id);
        $likes =  DB::table('likes')->where('user_id',"=",$id)->where('liked','=',1)->get()->count();
        $dislikes =  DB::table('likes')->where('art_id',"=",$id)->where('liked','=',0)->get()->count();
        $score = $likes - $dislikes;
        $user['score'] = $score;
        return response($user);
    }

    public function getUserWithArt(){
        $userId = Auth::id();
        $user = User::with('art')->find($userId);
        return response($user);
    }

    public function updateDesc(Request $request){
        $userId = Auth::id();
        $user = User::findOrFail($userId);
        $user->description = $request->description;
        $user->update();
        return response($user);
    }
}
