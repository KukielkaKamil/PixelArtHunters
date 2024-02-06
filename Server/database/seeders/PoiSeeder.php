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
                    'name' => 'Milenium Hall', 'latitude'=>'50.027960', 'longitude'=>'22.013198', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Uniwersytet Rzeszowski', 'latitude'=>'50.029543', 'longitude'=>'22.014948', 'modifier'=>'1.20'
                ],
                [
                    'name' => 'Pomnik Czynu Rewolucyjnego', 'latitude'=>'50.04061355282391', 'longitude'=>'21.999505041459397', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Zamek Lubomirskich', 'latitude'=>'50.03246814757948', 'longitude'=>'22.000393425395508', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Okrągła kładka dla pieszych', 'latitude'=>'50.04091565184865', 'longitude'=>'22.002741461585597', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Podkarpacki Urząd Wojewódzki', 'latitude'=>'50.04032630737545', 'longitude'=>'22.002876315423467', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Rynek miejski w Rzeszowie', 'latitude'=>'50.03779736056097', 'longitude'=>'22.00450420408585', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Rzeszowskie Bulwary', 'latitude'=>'50.04091565184865', 'longitude'=>'22.0037803790668', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Fontanna Multimedialna', 'latitude'=>'50.03395353384196', 'longitude'=>'22.0059999393616', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Rzeszów Główny', 'latitude'=>'50.0428315641663', 'longitude'=>'22.007387343332017', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Galeria Rzeszów', 'latitude'=>'50.042054584187206', 'longitude'=>'21.999521035719717', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Hala Podpromie w Rzeszowie', 'latitude'=>'50.02977981275763', 'longitude'=>'22.003795472333323', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Plaza Rzeszów', 'latitude'=>'50.020868991006886', 'longitude'=>'22.01999569624233', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Outlet Graffica', 'latitude'=>'50.03596267858152', 'longitude'=>'21.998220994476473', 'modifier'=>'1.00'
                ],
                [
                    'name' => '„Grzybek” Altanka Studencka', 'latitude'=>'50.0196091707797', 'longitude'=>'21.984161775684935', 'modifier'=>'1.00'
                ],
                [
                    'name' => 'Politechnika Rzeszowska', 'latitude'=>'50.01930847107855', 'longitude'=>'21.98928632229413', 'modifier'=>'1.00'
                ]
            ]
        );
    }
}
