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
    protected $fillable = ['size','image', 'reported'];

    public function poi(): BelongsTo
    {
        return $this->belongsTo(Poi::class);
    }

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }

    public function likes()
    {
        return $this->belongsToMany(User::class, 'likes', 'art_id', 'user_id')
            ->withPivot('liked');
    }
}
