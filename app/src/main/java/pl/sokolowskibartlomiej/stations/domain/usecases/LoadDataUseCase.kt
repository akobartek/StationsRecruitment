package pl.sokolowskibartlomiej.stations.domain.usecases

import pl.sokolowskibartlomiej.stations.domain.repository.StationsRepository

class LoadDataUseCase(private val stationsRepository: StationsRepository) {
    suspend operator fun invoke() = stationsRepository.loadData()
}