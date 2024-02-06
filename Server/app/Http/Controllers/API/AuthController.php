<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Laravel\Sanctum\PersonalAccessToken;


class AuthController extends Controller
{
    public function register(Request $request)
    {
        $credentials = $request->validate([
            'name' => 'required|string',
            'email' => 'required|email|unique:users,email',
            'password' => 'required|string',
        ]);

        $user = User::create([
            'name' => $credentials['name'],
            'email' => $credentials['email'],
            'password' => Hash::make($credentials['password']),
            'score' => 0
        ]);

        // $token = $user->createToken('authToken')->plainTextToken;

        $response = [
            'user' => $user,
            // 'token' => $token
        ];
        return response($response, 201);
    }

    public function login(Request $request)
    {
        $credentials = $request->validate([
            'email' => 'required|email',
            'password' => 'required|string',
        ]);

        $user = User::where('email',$credentials['email'])->first();

            if(!$user || !Hash::check($credentials['password'], $user->password)){
                return response([
                    'message' => 'Wrong creditentials'
                ], 401);
            }

            $token = $user->createToken('authToken')->plainTextToken;

            $response = [
                'user' => $user,
                'token' => $token
            ];
            return response($response, 201);
    }

    public function logout(Request $request)
    {
        // Igonore this error
        auth('sanctum')->user()->tokens()->delete();

        return response(['message' => 'loged out'], 200);
    }


}
