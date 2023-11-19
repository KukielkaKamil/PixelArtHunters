<?php

namespace Database\Seeders;

use App\Models\Poi;
use Illuminate\Database\Seeder;

class PoiSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        Poi::insert(
            [
                [
                    'name' => 'Milenium Hall', 'longitude'=>'50.027960', 'latitude'=>'22.013198', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Uniwersytet Rzeszowski', 'longitude'=>'50.029543', 'latitude'=>'22.014948', 'modifier'=>'1.20'
                ]
            ]
        );
    }
}
