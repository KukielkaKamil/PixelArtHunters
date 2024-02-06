<?php

namespace App\Http\Controllers\API;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Models\Poi;
use App\Models\User;

class PoiController extends Controller
{
    public function index(){
        $pois = Poi::all();
        return response() -> json($pois);
    }

    public function store(Request $request){

        $poi = new Poi;
        $input = $request->all();
        $poi::create($input);
    }

    public function update(Request $request, int $id){
        $poi = Poi::findOrFail($id);
        $input = $request->all();
        $poi->update($input);
    }

    public function destroy(int $id){
        $poi = Poi::findOrFail($id);
        $poi->delete();
    }

    public function poiArtList(int $id){
        $poi = POI::with('art.user')->find($id);
        $art = $poi->art;
        return response() -> json($art);
    }
}
