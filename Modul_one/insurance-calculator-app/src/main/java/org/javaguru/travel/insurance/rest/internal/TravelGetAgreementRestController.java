package org.javaguru.travel.insurance.rest.internal;

import com.google.common.base.Stopwatch;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreResult;
import org.javaguru.travel.insurance.core.services.TravelGetAgreementService;
import org.javaguru.travel.insurance.dto.internal.GetAgreementDtoConverter;
import org.javaguru.travel.insurance.dto.internal.TravelGetAgreementResponse;
import org.javaguru.travel.insurance.rest.common.TravelRestRequestExecutionTimeLogger;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insurance/travel/api/internal/agreement")
public class TravelGetAgreementRestController {

	private final TravelGetAgreementRequestLogger requestLogger;
	private final TravelGetAgreementResponseLogger responseLogger;
	private final TravelRestRequestExecutionTimeLogger executionTimeLogger;
	private final TravelGetAgreementService getAgreementService;
	private final GetAgreementDtoConverter dtoConverter;


	public TravelGetAgreementRestController(TravelGetAgreementRequestLogger requestLogger,
											TravelGetAgreementResponseLogger responseLogger,
											TravelRestRequestExecutionTimeLogger executionTimeLogger,
											TravelGetAgreementService getAgreementService,
											GetAgreementDtoConverter dtoConverter) {
		this.requestLogger = requestLogger;
		this.responseLogger = responseLogger;
		this.executionTimeLogger = executionTimeLogger;
		this.getAgreementService = getAgreementService;
		this.dtoConverter = dtoConverter;
	}

	@GetMapping(path = "/{uuid}",
			produces = "application/json")
	public TravelGetAgreementResponse getAgreement(@PathVariable("uuid") String uuid) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		TravelGetAgreementResponse response = processRequest(uuid);
		executionTimeLogger.logExecutionTime(stopwatch);
		return response;
	}

	private TravelGetAgreementResponse processRequest(String uuid) {
		requestLogger.log(uuid);
		TravelGetAgreementCoreCommand coreCommand = dtoConverter.buildCoreCommand(uuid);
		TravelGetAgreementCoreResult coreResult = getAgreementService.getAgreement(coreCommand);
		TravelGetAgreementResponse response = dtoConverter.buildResponse(coreResult);
		responseLogger.log(response);
		return response;
	}
}