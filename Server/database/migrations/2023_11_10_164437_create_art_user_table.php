<?php

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
        Schema::create('art_user', function (Blueprint $table) {
            $table->foreignId('pixel_art_id')->constrained();
            $table->foreignId('user_id')->constrained();
            $table->primary(['pixel_art_id','user_id']);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('art_user');
    }
};
