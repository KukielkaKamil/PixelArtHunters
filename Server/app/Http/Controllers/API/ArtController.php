<?php

namespace App\Http\Controllers\API;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Models\Art;
use App\Models\User;

class ArtController extends Controller
{
    public function index(){
        $art = Art::all();
        foreach ($art as $a){
            $a['owners'] = $a->user()->pluck('id');
        }
        return response() -> json($art);
    }

    public function store(Request $request){

        $art = new Art;
        $art->size = $request->size;
        $art->image = $request->image;
        $art -> poi_id = $request->poi_id;
        $owners_id = $request-> input('users', []);
        $owners = User::whereIn('id', $owners_id)->get();
        $art->save();
        $art -> user()->sync($owners);
    }

    public function update(Request $request, int $id){
        $art = Art::findOrFail($id);
        $art->size = $request->size;
        $art->image = $request->image;
        $art -> poi_id = $request->poi_id;
        $owners_id = $request-> input('users', []);
        $owners = User::whereIn('id', $owners_id)->get();
        $art->save();
        $art -> user()->sync($owners);
    }

    public function destroy(int $id){
        $art = Art::findOrFail($id);
        $art->delete();
    }
}
