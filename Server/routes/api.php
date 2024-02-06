<?php

use App\Http\Controllers\Api\ArtController;
use App\Http\Controllers\API\PoiController;
use App\Http\Controllers\API\AuthController;
use App\Http\Controllers\API\UserController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

// Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
//     return $request->user();
// });
Route::post('/login', [AuthController::class, 'login'])->name('login');
Route::post('/register', [AuthController::class, 'register']);


Route::group(['middleware' => ['auth:sanctum']], function () {
    // Route::get('/art', [ArtController::class, 'index']);
    Route::get('/logout', [AuthController::class, 'logout']);
    Route::post('/art/{id}/react', [ArtController::class, 'reaction']);
    Route::get('/user', [UserController::class, 'getUser']);
    Route::get('/user/art', [UserController::class, 'getUserWithArt']);
    Route::patch('/user/pixels', [UserController::class, 'updatePixels']);
    Route::get('/user/shake', [UserController::class, 'hasShaken']);
    Route::patch('/user/desc', [UserController::class, 'updateDesc']);
    Route::get('/user/{id}', [UserController::class, 'getUserWithId']);
});
Route::get('/art', [ArtController::class, 'index']);
Route::post('/art/create', [ArtController::class, 'store']);
Route::patch('/art/{id}', [ArtController::class, 'update']);
Route::delete('/art/{id}', [ArtController::class, 'destroy']);
Route::get('/art/reported', [ArtController::class, 'reported']);
Route::get('/art/{id}/score', [ArtController::class, 'score']);


Route::get('/poi', [PoiController::class, 'index']);
Route::post('/poi/create', [PoiController::class, 'store']);
Route::patch('/poi/{id}', [PoiController::class, 'update']);
Route::delete('/poi/{id}', [PoiController::class, 'destroy']);
Route::get('/poi/{id}/art', [PoiController::class, 'poiArtList']);
