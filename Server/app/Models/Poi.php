<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Poi extends Model
{
    use HasFactory;

    public $timestamps = false;
    protected $fillable = ['name', 'longitude', 'latitude', 'modifier'];

    public function art() :HasMany{
        return $this->hasMany(Art::class);
    }
}
