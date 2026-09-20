package com.creative.isitvegan.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.isitvegan.data.local.FeatureType
import com.creative.isitvegan.data.local.UsageManager
import com.creative.isitvegan.domain.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanItemViewModel @Inject constructor(
    private val repository: Repository,
    private val usageManager: UsageManager
) : ViewModel() {

    private val _isScanComplete = MutableStateFlow(false)
    val isScanComplete = _isScanComplete.asStateFlow()

    private val _barcodeResult = MutableStateFlow<String?>(null)
    val barcodeResult = _barcodeResult.asStateFlow()

    private val _existsInDb = MutableStateFlow<Boolean?>(null)
    val existsInDb = _existsInDb.asStateFlow()

    private val _quotaExhausted = MutableStateFlow<FeatureType?>(null)
    val quotaExhausted = _quotaExhausted.asStateFlow()

    val remainingScans: StateFlow<Int> = usageManager.getRemainingUsage(FeatureType.SCANNER)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureType.SCANNER.limit)

    val remainingManual: StateFlow<Int> = usageManager.getRemainingUsage(FeatureType.MANUAL_ENTRY)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureType.MANUAL_ENTRY.limit)

    fun onBarcodeDetected(code: String) {
        if (!_isScanComplete.value) {
            viewModelScope.launch {
                if (usageManager.canUseFeature(FeatureType.SCANNER)) {
                    _isScanComplete.value = true
                    val product = repository.getProductFromDb(code)
                    _existsInDb.value = product != null
                    _barcodeResult.value = code
                    usageManager.incrementUsage(FeatureType.SCANNER)
                } else {
                    _quotaExhausted.value = FeatureType.SCANNER
                }
            }
        }
    }

    fun onManualSearchTriggered(code: String) {
        viewModelScope.launch {
            if (usageManager.canUseFeature(FeatureType.MANUAL_ENTRY)) {
                _isScanComplete.value = true
                val product = repository.getProductFromDb(code)
                _existsInDb.value = product != null
                _barcodeResult.value = code
                usageManager.incrementUsage(FeatureType.MANUAL_ENTRY)
            } else {
                _quotaExhausted.value = FeatureType.MANUAL_ENTRY
            }
        }
    }

    fun resetScanner() {
        _isScanComplete.value = false
        _barcodeResult.value = null
        _existsInDb.value = null
        _quotaExhausted.value = null
    }

    fun clearBarcodeResult() {
        _barcodeResult.value = null
        // Note: we keep _existsInDb state until resetScanner is called for a new scan
    }
}
