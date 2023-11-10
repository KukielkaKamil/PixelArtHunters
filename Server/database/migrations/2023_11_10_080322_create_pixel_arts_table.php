<?php

use App\Models\PixelStop;
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('pixel_arts', function (Blueprint $table) {
            $table->id();
            $table->smallInteger('size');
            $table->binary('image');
            $table->foreignIdFor(PixelStop::class)->constrained();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('pixel_arts');
    }
};
