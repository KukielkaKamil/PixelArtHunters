<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class ArtUserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        DB::table('art_user')->insert(
            [
                [
                    'pixel_art_id'=>'1','user_id'=>'1'
                ],
                [
                    'pixel_art_id'=>'2','user_id'=>'2'
                ],
                [
                    'pixel_art_id'=>'3','user_id'=>'1'
                ],
            ]
        );
    }
}
