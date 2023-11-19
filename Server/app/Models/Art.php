<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class Art extends Model
{
    use HasFactory;

    public $timestamps = false;
    protected $fillable = ['size','image'];

    public function pixelStop(): BelongsTo
    {
        return $this->belongsTo(Poi::class);
    }

   public function user(): BelongsToMany
    {
        return $this->belongsToMany(User::class, 'art_users', 'art_id', 'user_id');
    }
}
