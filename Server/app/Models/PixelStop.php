<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;

class PixelStop extends Model
{
    use HasFactory;
    public $timestamps = false;
    protected $fillable = ['name', 'longitude', 'latitude'];

    public function pixelArt() :HasMany{
        return $this->hasMany(PixelArt::class);
    }
}
