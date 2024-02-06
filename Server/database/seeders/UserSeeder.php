<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        User::insert(
            [
                [
                    'name' => 'Jan','email' => 'j.grzebyk@gmail.com','password' => Hash::make('1234'),
                    'score' => '3100','is_admin' => '1'
                ],
                [
                    'name' => 'Adam','email' => 'a.pataluch@gmail.com','password' => Hash::make('1234'),
                    'score' => '3100', 'is_admin' => '0'
                ]
            ]
        );
    }
}
