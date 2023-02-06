package br.com.thiaago.tregion.model.controller

import br.com.thiaago.tregion.model.region.RegionPlayer

class RegionController(val players: MutableMap<String, RegionPlayer> = emptyMap<String, RegionPlayer>().toMutableMap())