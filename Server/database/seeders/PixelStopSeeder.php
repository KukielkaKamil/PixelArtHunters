<?php

namespace Database\Seeders;

use App\Models\PixelStop;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class PixelStopSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        PixelStop::insert(
            [
                [
                    'name' => 'Milenium Hall', 'longitude'=>'50.027960', 'latitude'=>'22.013198'
                ],
                [
                    'name' => 'Uniwersytet Rzeszowski', 'longitude'=>'50.029543', 'latitude'=>'22.014948'
                ]
            ]
        );
    }
}
